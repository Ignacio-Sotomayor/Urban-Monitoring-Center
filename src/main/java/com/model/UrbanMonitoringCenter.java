package com.model;
import com.DAO.InfractionTypesDAO;
import com.DAO.OwnersDAO;
import com.DAO.SecurityNoticeDAO;
import com.model.Automobile.MotorVehicleRegistry;
import com.model.Devices.*;
import com.model.Fines.ExcessiveSpeed;
import com.model.Fines.InfractionType;

import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UrbanMonitoringCenter {

    // --- Nested Class for Failure Records ---
    public static class FailureRecord {
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

    private HashMap<UUID,Device> devices;
    private Set<SecurityNotice> securityNotices;
    private Map<String,InfractionType> infractionTypes;
    private Set<UUID> fatalErrorDevices;
    private Queue<FailureRecord> failureRecords;

    private static UrbanMonitoringCenter instance=null;

    private UrbanMonitoringCenter(){
        devices = new HashMap<>();
        securityNotices = new TreeSet<>();
        infractionTypes = new HashMap<>();
        fatalErrorDevices = new HashSet<>();
        failureRecords = new ConcurrentLinkedQueue<>();
    }

    public Device getSpecificDevice(UUID uuid) {
        return devices.get(uuid);
    }

    public void addSecurityNotice(SecurityNotice notice){ securityNotices.add(notice); }

    //getters
    public static UrbanMonitoringCenter getUrbanMonitoringCenter(){
        if(instance == null)
            instance = new UrbanMonitoringCenter();
        return instance;
    }
    public InfractionType getSpecificInfractionType(String key){ return infractionTypes.get(key); }
    public HashMap<UUID,Device> getDevices() { return devices; }
    public Iterator<SecurityNotice> getSecurityNotices() { return securityNotices.iterator(); }
    public Queue<FailureRecord> getFailureRecords() { return failureRecords; }

    public FineIssuerDevice getRandomFineIssuerDevice() {
        List<Device> fineIssuers = devices.values().stream()
                .filter(d -> d instanceof FineIssuerDevice)
                .collect(Collectors.toList());

        if (fineIssuers.isEmpty()) {
            throw new IllegalStateException("No FineIssuerDevice found in the device list.");
        }

        Random random = new Random();
        return (FineIssuerDevice) fineIssuers.get(random.nextInt(fineIssuers.size()));
    }

    public void issuedDevices(Device d){
        if (d != null && d.getState()) {
            d.setState(false);
            failureRecords.add(new FailureRecord(d, "NO OPERATIVO"));
        }
    }
    public void repairDevices(Device d){
        if (d == null) return;
        d.setState(true);
        if (d instanceof TrafficLightController) {
            ((TrafficLightController) d).setForceIntermittent(false);
            ((TrafficLightController) d).setGreenWaveState(TrafficLightState.RED, TrafficLightState.RED);
        }
        if (isFatalError(d)) {
            this.fatalErrorDevices.remove(d.getId());
        }
    }

    public void repairToIntermittent(Device d) {
        if (d instanceof TrafficLightController) {
            repairDevices(d); 
            ((TrafficLightController) d).setForceIntermittent(true);
        }
    }

    public void setFatalError(Device device) {
        if (device != null && !isFatalError(device)) {
            if (device instanceof TrafficLightController) {
                ((TrafficLightController) device).setGreenWaveState(TrafficLightState.UNKNOWN, TrafficLightState.UNKNOWN);
            } else {
                this.fatalErrorDevices.add(device.getId());
            }
            issuedDevices(device); 
            failureRecords.add(new FailureRecord(device, "ERROR FATAL"));
        }
    }

    public boolean isFatalError(Device device) {
        if (device == null) return false;
        if (device instanceof TrafficLightController) {
            return ((TrafficLightController) device).getIntersectionLights().get(0).getCurrentState() == TrafficLightState.UNKNOWN;
        }
        return this.fatalErrorDevices.contains(device.getId());
    }

    public static void Initialize(){
        UrbanMonitoringCenter UMC = getUrbanMonitoringCenter();
        UMC.deserializeAllDevices("C:/Users/nacho/Downloads/Urban-Monitoring-Center-view-DB (2)/Urban-Monitoring-Center-view-DB/src/main/resources/devices.ser");
        UMC.startSimulations();
    }

    public void startSimulations() {
        // Independencia Wave
        List<TrafficLightController> independenceTrafficLights = devices.values().stream()
            .filter(d -> d instanceof TrafficLightController && d.getAddress().contains("Av. Independencia"))
            .map(d -> (TrafficLightController) d)
            .sorted(Comparator.comparing(d -> d.getLocation().getLatitude(), Comparator.reverseOrder()))
            .collect(Collectors.toList());

        if (!independenceTrafficLights.isEmpty()) {
            new Thread(() -> runGreenWave(independenceTrafficLights)).start();
        }

        // Rivadavia Wave
        List<TrafficLightController> rivadaviaTrafficLights = devices.values().stream()
            .filter(d -> d instanceof TrafficLightController && d.getAddress().contains("Rivadavia"))
            .map(d -> (TrafficLightController) d)
            .sorted(Comparator.comparing(d -> d.getLocation().getLongitude(), Comparator.reverseOrder()))
            .collect(Collectors.toList());

        if (!rivadaviaTrafficLights.isEmpty()) {
            new Thread(() -> runGreenWave(rivadaviaTrafficLights)).start();
        }
    }

    private void runGreenWave(List<TrafficLightController> trafficLights) {
        long stepDelay = 500;
        try {
            for (TrafficLightController tlc : trafficLights) {
                tlc.setGreenWaveState(TrafficLightState.GREEN, TrafficLightState.RED);
            }

            while (true) {
                for (TrafficLightController tlc : trafficLights) {
                    if (tlc.isIntermittentTime()) {
                        tlc.setGreenWaveState(TrafficLightState.INTERMITTENT, TrafficLightState.INTERMITTENT);
                    } else {
                        tlc.setGreenWaveState(TrafficLightState.YELLOW, TrafficLightState.RED);
                    }
                    Thread.sleep(stepDelay);
                }
                Thread.sleep(2000); 

                for (TrafficLightController tlc : trafficLights) {
                    if (tlc.isIntermittentTime()) {
                        tlc.setGreenWaveState(TrafficLightState.INTERMITTENT, TrafficLightState.INTERMITTENT);
                    } else {
                        tlc.setGreenWaveState(TrafficLightState.RED, TrafficLightState.RED);
                    }
                    Thread.sleep(stepDelay);
                }
                Thread.sleep(1000); 

                for (TrafficLightController tlc : trafficLights) {
                    if (tlc.isIntermittentTime()) {
                        tlc.setGreenWaveState(TrafficLightState.INTERMITTENT, TrafficLightState.INTERMITTENT);
                    } else {
                        tlc.setGreenWaveState(TrafficLightState.RED, TrafficLightState.GREEN);
                    }
                    Thread.sleep(stepDelay);
                }
                Thread.sleep(5000); 
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void serializeAllDevices(String fileName){
        try(ObjectOutputStream oop = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oop.writeObject(devices);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deserializeAllDevices (String fileName) {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))){
            HashMap<UUID,Device> loadedDevices = (HashMap<UUID, Device>) ois.readObject();
            this.devices = loadedDevices;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}