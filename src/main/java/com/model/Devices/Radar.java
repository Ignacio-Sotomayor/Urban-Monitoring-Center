package com.model.Devices;

import com.DAO.AutomobileDAO;
import com.DAO.FineDAO;
import com.model.Automobile.Automobile;
import com.controller.UrbanMonitoringCenter;

import java.io.Serial;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class Radar extends FineIssuerDevice {
    @Serial
    private static final long serialVersionUID = -515232461427930552L;
    private double speedLimit;

    public Radar( String address, Location location,boolean state, double speedLimit) {
        super(address, location,state, UrbanMonitoringCenter.getUrbanMonitoringCenter().getSpecificInfractionType("ExcessiveSpeed"));
        this.speedLimit = speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }
    public void issueFine(Automobile a, double speed) {
        if (speed > speedLimit) {
            try{
            AutomobileDAO automobileDao = new AutomobileDAO();
            Integer automobileId = automobileDao.getAutomobileIdByLicensePlate(a.getLicensePlate());
            automobileDao =null;

            FineDAO fineDao = new FineDAO();
            fineDao.insertSpeedingFine(super.getEmitedInfractionType().getAmount(),super.getEmitedInfractionType().getScoring(),
                    super.getLocation().getLatitude(),super.getLocation().getLongitude(),super.getAddress(), Timestamp.from(Instant.now()),
                    super.getId().toString(),automobileId,Integer.parseInt(String.valueOf(this.speedLimit)),Integer.parseInt(String.valueOf(speed)));
            fineDao = null;
        }catch(SQLException e){
                throw new RuntimeException(e);
            }
        }
    }
}