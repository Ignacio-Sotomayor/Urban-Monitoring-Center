package com.model;

import com.controller.UrbanMonitoringCenter;
import com.model.Automobile.MotorVehicleRegistry;
import com.model.Devices.Device;
import com.model.Devices.FineIssuerDevice;
import com.model.Devices.Location;
import com.model.Fines.EventGeolocation;
import com.model.Fines.InfractionType;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class Main {
    public static void main(String[] args) {
        UrbanMonitoringCenter.lastStateStart();
        MotorVehicleRegistry MVR = MotorVehicleRegistry.getMotorVehicleRegistry();
        UrbanMonitoringCenter UMC = UrbanMonitoringCenter.getUrbanMonitoringCenter();

        MVR.loadAutomobilesFromDB();
        UMC.loadInfractionTypes();


        UMC.startRandomFineSimulation();
        // Mantener el programa vivo 3 segundos:
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UMC.stopRandomFineSimulation();
        //MVR.informFines();
    }
}