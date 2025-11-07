package com.model.Devices;

import com.DAO.AutomobileDAO;
import com.DAO.FineDAO;
import com.model.Automobile.Automobile;
import com.model.Fines.EventGeolocation;
import com.model.Fines.ExcessiveSpeedFine;
import com.model.Fines.Fine;
import com.model.Fines.InfractionType;
import com.model.UrbanMonitoringCenter;

import java.io.Serial;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;

import static org.controlsfx.glyphfont.FontAwesome.Glyph.RANDOM;

public class Radar extends FineIssuerDevice {
    @Serial
    private static final long serialVersionUID = -515232461427930552L;
    private double speedLimit;

    private static final Random RANDOM = new Random();
    private static final double MIN_EXCESS = 1.0;
    private static final double MAX_EXCESS = 70.0;

    public Radar(String address, Location location, boolean state, InfractionType infractionType, double speedLimit) {
        super(address, location, state, infractionType);
        this.speedLimit = speedLimit;
    }

    @Override
    public String getDefaultInfractionTypeName() {
        return "Speeding";
    }

    public  double generateSpeedAboveLimit() {
        double excess = MIN_EXCESS + (MAX_EXCESS - MIN_EXCESS) * RANDOM.nextDouble();
        return getSpeedLimit() + excess;
    }

    public double getSpeedLimit() {return speedLimit;}
    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }
    @Override
    public void issueFine(Automobile a) {
        double speed = generateSpeedAboveLimit();
        System.out.println("Velocidad "+speed+" y vmax"+getSpeedLimit()+"\n");
        if (speed > speedLimit) {
            ExcessiveSpeedFine fine = new ExcessiveSpeedFine(new EventGeolocation(LocalDateTime.now(), getAddress(), getLocation(), this),
                    getEmitedInfractionType(), a, getSpeedLimit(), speed);
            try{
            AutomobileDAO automobileDao = new AutomobileDAO();
            Integer automobileId = automobileDao.getAutomobileIdByLicensePlate(a.getLicensePlate());
            automobileDao =null;

            FineDAO fineDao = new FineDAO();
            fineDao.insertSpeedingFine(super.getEmitedInfractionType().getAmount(),super.getEmitedInfractionType().getScoring(),
                    super.getLocation().getLatitude(),super.getLocation().getLongitude(),super.getAddress(), Timestamp.from(Instant.now()),
                    super.getId().toString(),automobileId,getSpeedLimit(), speed);
            fineDao = null;
        }catch(SQLException e){
                throw new RuntimeException(e);
            } finally {
            notifier.notifyObservers(fine);
        }
        }
    }
}