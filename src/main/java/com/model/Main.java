package com.model;

import com.model.Automobile.MotorVehicleRegistry;
import com.model.Devices.Device;
import com.model.Devices.FineIssuerDevice;
import com.model.Devices.Location;
import com.model.Fines.EventGeolocation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class Main {
    public static void main(String[] args) {
        UrbanMonitoringCenter.Initialize();
        MotorVehicleRegistry MVR = MotorVehicleRegistry.getMotorVehicleRegistry();
        UrbanMonitoringCenter UMC = UrbanMonitoringCenter.getUrbanMonitoringCenter();
        // UMC.serializeAllDevices("devices.ser");
        // UMC.deserializeAllDevices("devices.ser");
         UMC.showDevices();


        FineIssuerDevice fineIssuer;
        for (int i = 0; i < 10; i++) {
            fineIssuer = UMC.getRandomFineIssuerDevice();
            fineIssuer.issueFine(MVR.getRandomAutomobile());
        }

        Device d;
        for (int i = 0; i < 4; i++) {
            d=UMC.getRandomDevice();
            d.fail();
        }

        UMC.addSecurityNotice(new SecurityNotice("Ambulance call ",new EventGeolocation(LocalDateTime.of(LocalDate.of(2019,9,26),LocalTime.of(21,17,45)), "Fasta University",new Location(-39.895647,-59.998765), UMC.getRandomDevice())));
        UMC.addSecurityNotice(new SecurityNotice("Firefighters call ",new EventGeolocation(LocalDateTime.now(), "Fasta Universidad",new Location(-39.895647,-59.998765), UMC.getRandomDevice())));

        UMC.informAllSecurityNotices();
        MVR.showAllAutomobiles();
        MVR.informFines();

    }
}