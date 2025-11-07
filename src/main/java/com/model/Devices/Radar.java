package com.model.Devices;

import com.DAO.AutomobileDAO;
import com.DAO.FineDAO;
import com.model.Automobile.Automobile;
import com.controller.UrbanMonitoringCenter;
import com.model.Fines.ExcessiveSpeed;

import java.io.Serial;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;

public class Radar extends FineIssuerDevice {
    @Serial
    private static final long serialVersionUID = -515232461427930552L;
    private double speedLimit;

    private static final int MIN_EXCESS = 6;
    private static final int MAX_EXCESS = 30;
    private static final Random RANDOM = new Random();

    public Radar( String address, Location location,boolean state, double speedLimit) {
        super(address, location,state, UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("Speeding"));
        this.speedLimit = speedLimit;
    }

    public void setEmittedInfractionType(){
        super.setEmittedInfractionType(UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("Speeding"));
    }

    public double generateRandomVehicleSpeed() {
        double excess = MIN_EXCESS + RANDOM.nextDouble() * (MAX_EXCESS - MIN_EXCESS);
        return speedLimit + excess;
    }

    public double getSpeedLimit() {return speedLimit;}
    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }
    @Override
    public void issueFine(Automobile a) {
        int speed =(int)generateRandomVehicleSpeed();
        if (speed > getSpeedLimit()) {
            ExcessiveSpeed infractionType = (ExcessiveSpeed) getEmitedInfractionType();
            Object[] fineData = infractionType.calculateFineForSpeed(speed, getSpeedLimit());
            BigDecimal amount = (BigDecimal) fineData[0];
            int score = (int) fineData[1];
            try{
            AutomobileDAO automobileDao = new AutomobileDAO();
            Integer automobileId = automobileDao.getAutomobileIdByLicensePlate(a.getLicensePlate());
            automobileDao =null;

            FineDAO fineDao = new FineDAO();
            fineDao.insertSpeedingFine(amount,score, super.getLocation().getLatitude(),super.getLocation().getLongitude(),super.getAddress(), Timestamp.from(Instant.now()),
                    super.getId().toString(),automobileId,1,(int)getSpeedLimit(),speed);
            fineDao = null;
        }catch(SQLException e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getIconPath() {
        String path = (getState())?"/Icons/OperativeRadar.png" : "/Icons/InoperativeRadar.png";
        URL resource = getClass().getResource(path);
        return resource != null ? resource.toExternalForm() : "";
    }

    @Override
    public String getDeviceTypeName() {
        return "Radar";
    }

    @Override
    public String getDeviceSpecificInfo() {
        return "<br>Límite de velocidad: " + speedLimit + " km/h";
    }
}