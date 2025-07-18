package model.Devices;

import model.Automobile.Automobile;
import model.Automobile.MotorVehicleRegistry;
import model.Fines.EventGeolocation;
import model.Fines.Fine;
import model.Fines.InfractionType;

import java.time.LocalDateTime;


public abstract class FineIssuerDevice extends Device{
    private InfractionType emitedInfractionType;

    public FineIssuerDevice(String address, Location location, InfractionType emitedInfractionType) {
        super(address, location);
        this.emitedInfractionType = emitedInfractionType;
    }

    public void issueFine(Automobile a){
        MotorVehicleRegistry MVR = MotorVehicleRegistry.getMotorVehicleRegistry();
        Fine fine = new Fine( new EventGeolocation(LocalDateTime.now(),super.getAddress(), super.getLocation(), this), emitedInfractionType,a);
        MVR.addFineToAutomobile(a, fine);
    }
}