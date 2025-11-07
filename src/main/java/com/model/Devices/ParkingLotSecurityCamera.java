package com.model.Devices;

import com.model.Fines.InfractionType;
import com.model.UrbanMonitoringCenter;

import java.io.Serial;
import java.time.Duration;

public class ParkingLotSecurityCamera extends FineIssuerDevice {
    @Serial
    private static final long serialVersionUID = -8597720654876321354L;
    private Duration toleranceTime;

    public ParkingLotSecurityCamera(String address, Location location, boolean state, InfractionType infractionType, Duration toleranceTime) {
        super(address, location, state, infractionType);
        this.toleranceTime = toleranceTime;
    }

    @Override
    public String getDefaultInfractionTypeName() {
        return "ParkingOverTime";
    }

}