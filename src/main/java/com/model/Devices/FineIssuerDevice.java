package com.model.Devices;

import com.DAO.AutomobileDAO;
import com.DAO.FineDAO;
import com.DAO.InfractionTypesDAO;
import com.model.Automobile.Automobile;
import com.model.Automobile.MotorVehicleRegistry;
import com.model.Fines.EventGeolocation;
import com.model.Fines.Fine;
import com.model.Fines.InfractionType;
import com.view.Reports.FinesWindow;
import com.view.FinesWindow;
import com.view.RegistryNotifier;

import java.io.Serial;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public abstract class FineIssuerDevice extends Device{
    @Serial
    private static final long serialVersionUID = 7716519194965191290L;
    private InfractionType emitedInfractionType;
    private transient final RegistryNotifier notifier;

    public FineIssuerDevice(String address, Location location,boolean state, InfractionType emitedInfractionType) {
        super(address, location, state);

        this.emitedInfractionType = emitedInfractionType;
        this.notifier = FinesWindow.getNotifier();
    }

    public InfractionType getEmitedInfractionType() {
        return emitedInfractionType;
    }

    public void issueFine(Automobile a){
        MotorVehicleRegistry MVR = MotorVehicleRegistry.getMotorVehicleRegistry();
        Fine fine = new Fine( new EventGeolocation(LocalDateTime.now(),super.getAddress(), super.getLocation(), this), emitedInfractionType,a);
        try{
            InfractionTypesDAO infractionDao = new InfractionTypesDAO();
            Integer infractionTypeID = infractionDao.getInfractionTypeIdByName(fine.getInfractionType().getName());
            infractionDao = null;

            AutomobileDAO automobileDao = new AutomobileDAO();
            Integer automobileID = automobileDao.getAutomobileIdByLicensePlate(fine.getAutomobile().getLicensePlate());
            automobileDao = null;

            FineDAO DAO = new FineDAO();
            DAO.insertFine(fine.getAmount(),fine.getScoring(),
                    fine.getEventGeolocation().getLocation().getLatitude(),fine.getEventGeolocation().getLocation().getLongitude(),
                    fine.getEventGeolocation().getAddress(), Timestamp.valueOf(fine.getEventGeolocation().getDateTime()),
                    super.getId().toString(),
                    infractionTypeID,
                    automobileID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            notifier.notifyObservers(fine);
        }

    }

}