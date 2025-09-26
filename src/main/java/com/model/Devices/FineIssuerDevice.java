package com.model.Devices;

import com.model.Automobile.Automobile;
import com.model.Automobile.MotorVehicleRegistry;
import com.model.Fines.EventGeolocation;
import com.model.Fines.Fine;
import com.model.Fines.InfractionType;

import java.time.LocalDateTime;


public abstract class FineIssuerDevice extends Device{
    private InfractionType emitedInfractionType;

    public FineIssuerDevice(String address, Location location,boolean state, InfractionType emitedInfractionType) {
        super(address, location, state);
        this.emitedInfractionType = emitedInfractionType;
    }

    public void issueFine(Automobile a){
        MotorVehicleRegistry MVR = MotorVehicleRegistry.getMotorVehicleRegistry();
        Fine fine = new Fine( new EventGeolocation(LocalDateTime.now(),super.getAddress(), super.getLocation(), this), emitedInfractionType,a);
        MVR.addFineToAutomobile(a, fine);
    }
    public void IssueFine(Automobile a){
        MotorVehicleRegistry MVR = MotorVehicleRegistry.getMotorVehicleRegistry();
        Fine fine = new Fine( new EventGeolocation(LocalDateTime.now(),super.getAddress(), super.getLocation(), this), emitedInfractionType,a);
        MVR.addFineToAutomobile(a, fine);
    }
}