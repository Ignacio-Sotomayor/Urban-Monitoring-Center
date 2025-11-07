package com.model;

import com.model.Devices.Device;

import java.time.LocalDateTime;

public class FailureRecord{
        private final Device device;
        private final String failureType;
        private final LocalDateTime timestamp;

        public FailureRecord(Device device, String failureType) {
            this.device = device;
            this.failureType = failureType;
            this.timestamp = LocalDateTime.now();
        }

        public Device getDevice() {
            return device;
        }

        public String getFailureType() {
            return failureType;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
