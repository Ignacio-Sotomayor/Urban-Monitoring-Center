package com.model;

import com.model.Devices.Device;

public class UnrepairableDeviceException extends RuntimeException {
    Device unrepairableDevice;
    public UnrepairableDeviceException(String message) {
        super(message);
    }
    public UnrepairableDeviceException(Device d,String message) {
        super(message);
        unrepairableDevice = d;
    }

    public Device getUnrepairableDevice() {
        return unrepairableDevice;
    }
}