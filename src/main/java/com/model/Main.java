package com.model;

import com.controller.UrbanMonitoringCenter;
import com.model.Automobile.MotorVehicleRegistry;


public class Main {
    public static void main(String[] args) {
        UrbanMonitoringCenter.lastStateStart();
        MotorVehicleRegistry MVR = MotorVehicleRegistry.getMotorVehicleRegistry();
        UrbanMonitoringCenter UMC = UrbanMonitoringCenter.getUrbanMonitoringCenter();

        MVR.loadAutomobilesFromDB();
        // UMC.saveDevicesState("devices.ser");
        // UMC.deserializeAllDevices("devices.ser");
        // UMC.showDevices();


        /*FineIssuerDevice fineIssuer;
        for (int i = 0; i < 10; i++) {
            fineIssuer = UMC.getRandomFineIssuerDevice();
            fineIssuer.issueFine(MVR.getRandomAutomobile());
        }
        */
        /*
        Device d;
        for (int i = 0; i < 4; i++) {
            d=UMC.getRandomDevice();
            d.fail();
        }
        */
        //UMC.addSecurityNotice(new SecurityNotice("Ambulance call ",new EventGeolocation(LocalDateTime.of(LocalDate.of(2019,9,26),LocalTime.of(21,17,45)), "Fasta University",new Location(-39.895647,-59.998765), UMC.getRandomDevice())));
        //UMC.addSecurityNotice(new SecurityNotice("Firefighters call ",new EventGeolocation(LocalDateTime.now(), "Fasta Universidad",new Location(-39.895647,-59.998765), UMC.getRandomDevice())));

       // UMC.informAllSecurityNotices(); rehabilitar luego
        System.out.println("Automobile \n");
        MVR.showAllAutomobiles();
        // MVR.informFines(); rehabilito luego
        //mostrar (auto,brand,owner,model),service
    }
}