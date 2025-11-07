package com.controller;
import com.DAO.FineDAO;
import com.DAO.InfractionTypesDAO;
import com.DAO.OwnersDAO;
import com.DAO.SecurityNoticeDAO;
import com.model.Automobile.Automobile;
import com.model.Automobile.MotorVehicleRegistry;
import com.model.Devices.*;
import com.model.FailureRecord;
import com.model.Fines.EventGeolocation;
import com.model.Fines.Fine;
import com.model.Fines.InfractionType;
import com.model.SecurityNotice;
import com.model.Service;
import java.util.Queue;

import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class UrbanMonitoringCenter {

    private HashMap<UUID,Device> devices;
    private Set<SecurityNotice> securityNotices;
    private Set<UUID> fatalErrorDevices;
    private Map<String,InfractionType> infractionTypes;
    private Queue<FailureRecord> failureRecords;

    private static UrbanMonitoringCenter instance=null;

    private Thread fineThread;
    private boolean running = false;
    private final Object fineSimulationLock = new Object();

    private Thread failureThread;
    private boolean failureRunning = false;
    private final Object failureSimulationLock = new Object();

    private static final int MIN_FINE_INTERVAL = 5;   // seconds
    private static final int MAX_FINE_INTERVAL = 15;  // seconds
    private static final int MIN_FAILURE_INTERVAL = 10; // seconds
    private static final int MAX_FAILURE_INTERVAL = 30; // seconds
    private static final double FATAL_ERROR_PROBABILITY = 0.2; // 20% chance for a fatal error

    private UrbanMonitoringCenter(){
        devices = new HashMap<>();
        securityNotices = new TreeSet<>();
        infractionTypes = new HashMap<>();
        failureRecords = new LinkedList<>();
        fatalErrorDevices = new HashSet<>();
    }

    public Device getSpecificDevice(UUID uuid) {
        return devices.get(uuid);
    }

    public void addSecurityNotice(SecurityNotice notice){ securityNotices.add(notice); }

    public void createAndSaveSecurityNotice(Device device, String description, Set<Service> services) {
        SecurityNoticeDAO securityNoticeDao = new SecurityNoticeDAO();
        try {
            securityNoticeDao.insertSecurityNotice(description, Timestamp.valueOf(LocalDateTime.now()), device.getAddress(), device.getLocation().getLatitude(), device.getLocation().getLongitude(), device.getId().toString(), services);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //getters
    public static UrbanMonitoringCenter getUrbanMonitoringCenter(){
        if(instance == null)
            instance = new UrbanMonitoringCenter();
        return instance;
    }
    public InfractionType getSpecificInfractionType(String key){ return infractionTypes.get(key); }
    public HashMap<UUID,Device> getDevices() { return devices; }
    public Iterator<SecurityNotice> getSecurityNotices() { return securityNotices.iterator(); }
    public List<FailureRecord> getFailureRecords() {
        // Return a copy to avoid ConcurrentModificationException
        return new ArrayList<>(failureRecords);
    }

    public Device getRandomDevice(){
        if (devices.isEmpty()) return null;
        List<Device> deviceList = new ArrayList<>(devices.values());
        return deviceList.get(new Random().nextInt(deviceList.size()));
    }

    public FineIssuerDevice getRandomFineIssuerDevice() {
        List<FineIssuerDevice> fineIssuers = devices.values().stream()
                .filter(d -> d instanceof FineIssuerDevice)
                .map(d -> (FineIssuerDevice) d)
                .collect(Collectors.toList());

        if (fineIssuers.isEmpty()) {
            throw new IllegalStateException("No FineIssuerDevice found in the device list.");
        }
        Random random = new Random();
        FineIssuerDevice d = fineIssuers.get(random.nextInt(fineIssuers.size()));
        d.setEmittedInfractionType();
        return d;
    }

    public void issuedDevices(Device d){
        if (d != null && d.getState()) {
            d.setState(false);
            failureRecords.add(new FailureRecord(d, "NO OPERATIVO"));
        }
        UrbanMonitoringCenter.getUrbanMonitoringCenter().saveDevices("devices.ser");
    }
    public void repairDevices(Device d){
        if (d == null) return;
        d.setState(true); // Mark the device as operative

        if (d.getDeviceTypeName() == "TrafficLightController") {
            TrafficLightController tlc = (TrafficLightController) d;
            tlc.getIntersectionLights().get(0).changeState(TrafficLightState.GREEN);
            tlc.getIntersectionLights().get(1).changeState(TrafficLightState.RED);
            // Remove from fatal errors if it was there
            this.fatalErrorDevices.remove(d.getId());
        } else {
            this.fatalErrorDevices.remove(d.getId());
        }
        UrbanMonitoringCenter.getUrbanMonitoringCenter().saveDevices("devices.ser");

    }

    public Device getDeviceByAddress(String address) throws SQLException {
        for (Device device : devices.values()) {
            if (device.getAddress().equalsIgnoreCase(address)) {
                return device;
            }
        }
        return null;
    }

    public static void baseBrandStart(){
        UrbanMonitoringCenter UMC = getUrbanMonitoringCenter();
        TrafficLightController tf;

        //TrafficLightControllers in Av Independencia
        tf = new TrafficLightController("Av. Independencia 2500",new Location(BigDecimal.valueOf(-38.002863),BigDecimal.valueOf(-57.558199)),true, new TrafficLight("Av. Independencia", "Av. Independencia", true, TrafficLightState.GREEN), new TrafficLight("Av. Independencia", "Gascon",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independencia 2400",new Location(BigDecimal.valueOf(-38.002053),BigDecimal.valueOf(-57.557535)),true,new TrafficLight("Av. Independencia", "Av. Independencia", true, TrafficLightState.GREEN), new TrafficLight("Av. Independencia", "Falucho",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independencia 2300",new Location(BigDecimal.valueOf(-38.001261),BigDecimal.valueOf(-57.556904)),true,new TrafficLight("Av. Independencia", "Av. Independencia", true, TrafficLightState.GREEN), new TrafficLight("Av. Independencia", "Almirante Brown",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independencia, Av. Colon",new Location(BigDecimal.valueOf(-38.000394),BigDecimal.valueOf(-57.556213)),true,new TrafficLight("Av. Independencia", "Av. Independencia", true, TrafficLightState.GREEN), new TrafficLight("Av. Independencia", "Av. Colon",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independencia 2100",new Location(BigDecimal.valueOf(-37.999542),BigDecimal.valueOf(-57.555559)),true,new TrafficLight("Av. Independencia", "Av. Independencia", true, TrafficLightState.GREEN), new TrafficLight("Av. Independencia", "Bolivar",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independencia 2000",new Location(BigDecimal.valueOf(-37.998758),BigDecimal.valueOf(-57.554895)),true,new TrafficLight("Av. Independencia", "Av. Independencia", true, TrafficLightState.GREEN), new TrafficLight("Av. Independencia", "Moreno",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independencia 1900",new Location(BigDecimal.valueOf(-37.997960),BigDecimal.valueOf(-57.554263)),true,new TrafficLight("Av. Independencia", "Av. Independencia", true, TrafficLightState.GREEN), new TrafficLight("Av. Independencia", "Belgrano",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independencia 1800",new Location(BigDecimal.valueOf(-37.997194),BigDecimal.valueOf(-57.553623)),true,new TrafficLight("Av. Independencia", "Av. Independencia", true, TrafficLightState.GREEN), new TrafficLight("Av. Independencia", "Rivadavia",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independencia 1700",new Location(BigDecimal.valueOf(-37.996395),BigDecimal.valueOf(-57.552932)),true,new TrafficLight("Av. Independencia", "Av. Independencia", true, TrafficLightState.GREEN), new TrafficLight("Av. Independencia", "San Martin",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independencia, Av. Pedro Luro",new Location(BigDecimal.valueOf(-37.995545),BigDecimal.valueOf(-57.552259)),true,new TrafficLight("Av. Independencia", "Av. Independencia", true, TrafficLightState.GREEN), new TrafficLight("Av. Independencia", "Av. Pedro Luro",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independencia 1500",new Location(BigDecimal.valueOf(-37.994681),BigDecimal.valueOf(-57.551600)),true,new TrafficLight("Av. Independencia", "Av. Independencia", true, TrafficLightState.GREEN), new TrafficLight("Av. Independencia", "25 de Mayo",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independencia 1400",new Location(BigDecimal.valueOf(-37.993883),BigDecimal.valueOf(-57.550930)),true,new TrafficLight("Av. Independencia", "Av. Independencia", true, TrafficLightState.GREEN), new TrafficLight("Av. Independencia", "9 de Julio",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independencia 1300",new Location(BigDecimal.valueOf(-37.993094),BigDecimal.valueOf(-57.550322)),true,new TrafficLight("Av. Independencia", "Av. Independencia", true, TrafficLightState.GREEN), new TrafficLight("Av. Independencia", "3 de Febrero",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independencia 1200",new Location(BigDecimal.valueOf(-37.992316),BigDecimal.valueOf(-57.549679)),true,new TrafficLight("Av. Independencia", "Av. Independencia", true, TrafficLightState.GREEN), new TrafficLight("Av. Independencia", "11 de Septiembre",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);

        //TrafficLightControllers in Rivadavia street
        tf = new TrafficLightController("Rivadavia 2900",new Location(BigDecimal.valueOf(-37.998781),BigDecimal.valueOf(-57.550557)),true,new TrafficLight("Rivadavia", "Rivadavia", true, TrafficLightState.GREEN), new TrafficLight("Rivadavia", "Hipolito Yrigoyen",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Rivadavia 2800",new Location(BigDecimal.valueOf(-37.999231),BigDecimal.valueOf(-57.549566)),true,new TrafficLight("Rivadavia", "Rivadavia", true, TrafficLightState.GREEN), new TrafficLight("Rivadavia", "Bartolome Mitre",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Rivadavia 2700",new Location(BigDecimal.valueOf(-37.999741),BigDecimal.valueOf(-57.548524)),true,new TrafficLight("Rivadavia", "Rivadavia", true, TrafficLightState.GREEN), new TrafficLight("Rivadavia", "San Luis",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Rivadavia 2600",new Location(BigDecimal.valueOf(-38.000255),BigDecimal.valueOf(-57.547554)),true,new TrafficLight("Rivadavia", "Rivadavia", true, TrafficLightState.GREEN), new TrafficLight("Rivadavia", "Cordoba",false, TrafficLightState.RED));
        UMC.devices.put(tf.getId(),tf);

        Radar rd;
        //Radars
        rd = new Radar("Av. Pedro Luro 3242",new Location(BigDecimal.valueOf(-37.995296),BigDecimal.valueOf(-57.552912)),true,60);
        UMC.devices.put(rd.getId(),rd);
        rd = new Radar("Av. Pedro Luro 2556",new Location(BigDecimal.valueOf(-37.998900),BigDecimal.valueOf(-57.545851)),true,60);
        UMC.devices.put(rd.getId(),rd);

        SecurityCamera secCamera;
        //Security Cameras
        secCamera = new SecurityCamera("Peatonal San Martin 2802",new Location(BigDecimal.valueOf(-37.998457),BigDecimal.valueOf(-57.549009)),true);
        UMC.devices.put(secCamera.getId(),secCamera);
        secCamera = new SecurityCamera("25 de Mayo 2700",new Location(BigDecimal.valueOf(-37.997282),BigDecimal.valueOf(-57.54628)),true);
        UMC.devices.put(secCamera.getId(),secCamera);
        secCamera = new SecurityCamera("Rivadavia 3300", new Location(BigDecimal.valueOf(-37.997841), BigDecimal.valueOf(-57.552919)), true);
        UMC.devices.put(secCamera.getId(), secCamera);

        ParkingLotSecurityCamera plCamera;
        //ParkingLotSecurityCameras
        plCamera = new ParkingLotSecurityCamera("Av. Colon 2900", new Location(BigDecimal.valueOf(-38.002099),BigDecimal.valueOf(-57.553031)),true, Duration.ofSeconds(1200));
        UMC.devices.put(plCamera.getId(),plCamera);

        UMC.saveDevices("devices.ser");
        UMC.loadInfractionTypes();
        UMC.startSimulations();
        UMC.startRandomFineSimulation();
        UMC.startRandomFailureSimulation();
    }
    public static void lastStateStart(){
        UrbanMonitoringCenter UMC = getUrbanMonitoringCenter();
        UMC.loadDevices("devices.ser");
        UMC.loadInfractionTypes();
        UMC.startSimulations();
        UMC.startRandomFineSimulation();
        UMC.startRandomFailureSimulation();
    }

    public void saveDevices(String fileName){
        try(ObjectOutputStream oop = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oop.writeObject(devices);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadDevices (String fileName) {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))){
            HashMap<UUID,Device> loadedDevices = (HashMap<UUID, Device>) ois.readObject();
            this.devices = loadedDevices;
            List<FineIssuerDevice> fineIssuers = devices.values().stream()
                    .filter(d -> d instanceof FineIssuerDevice)
                    .map(d -> (FineIssuerDevice) d)
                    .toList();
            for(FineIssuerDevice f: fineIssuers){
                f.setEmittedInfractionType();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadInfractionTypes() {
        InfractionTypesDAO dao = new InfractionTypesDAO();
        try {
            List<InfractionType> types = dao.getAllInfractionTypes();
            for (InfractionType t : types)
                infractionTypes.put(t.getName(), t);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showDevices(){
        for (Map.Entry<UUID, Device> d : devices.entrySet()) {
            System.out.println(d);
        }
    }

    public int insertOwner(String legalId, String fullName, String address) {
        try {
            OwnersDAO ownersDAO = new OwnersDAO();
            return ownersDAO.insertOwner(legalId, fullName, address);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void startRandomFineSimulation() {
        stopRandomFineSimulation();

        synchronized (fineSimulationLock) {
            running = true;
        }

        fineThread = new Thread(() -> {
            Random random = new Random();
            boolean isRunning;
            synchronized (fineSimulationLock) {
                isRunning = running;
            }

            while (isRunning) {
                try {
                    simulateRandomFineOnce();
                    int delay = random.nextInt(MAX_FINE_INTERVAL - MIN_FINE_INTERVAL + 1) + MIN_FINE_INTERVAL;
                    Thread.sleep(delay * 500);

                } catch (InterruptedException e) {
                    break;
                }
                synchronized (fineSimulationLock) {
                    isRunning = running;
                }
            }
        });

        fineThread.setDaemon(true);
        fineThread.setName("RandomFineGenerator");
        fineThread.start();
    }

    public void stopRandomFineSimulation() {
        synchronized (fineSimulationLock) {
            running = false;
        }
        if (fineThread != null) {
            fineThread.interrupt();
            fineThread = null;
        }
    }
    public void simulateRandomFineOnce() {
        MotorVehicleRegistry mvr = MotorVehicleRegistry.getMotorVehicleRegistry();

        FineIssuerDevice device;
        try {
            device = getRandomFineIssuerDevice();
            Automobile auto = mvr.getRandomAutomobile();
            if(device.getState() && auto!=null){
                device.issueFine(auto);
                System.out.println("Multa emitida por " + device.getAddress() + " a " + auto.getLicensePlate());
            }
        } catch (IllegalStateException e) {
            System.err.println("No hay dispositivos emisores disponibles.");
        }
    }

    public void startRandomFailureSimulation() {
        stopRandomFailureSimulation(); // Ensure only one is running

        synchronized (failureSimulationLock) {
            failureRunning = true;
        }

        failureThread = new Thread(() -> {
            Random random = new Random();
            boolean isRunning;
            synchronized (failureSimulationLock) {
                isRunning = failureRunning;
            }

            while (isRunning) {
                try {
                    int delay = random.nextInt(MAX_FAILURE_INTERVAL - MIN_FAILURE_INTERVAL + 1) + MIN_FAILURE_INTERVAL;
                    Thread.sleep(delay * 500);

                    Device deviceToFail = getRandomDevice();
                    if (deviceToFail != null) {
                        System.out.println("SIMULATING FAILURE for device: " + deviceToFail.getAddress());
                        if (Objects.equals(deviceToFail.getDeviceTypeName(), "TrafficLightController") && random.nextDouble() < FATAL_ERROR_PROBABILITY) {
                            setFatalError(deviceToFail);
                        } else {
                            issuedDevices(deviceToFail);
                        }
                    }

                } catch (InterruptedException e) {
                    break;
                }
                synchronized (failureSimulationLock) {
                    isRunning = failureRunning;
                }
            }
        });

        failureThread.setDaemon(true);
        failureThread.setName("RandomFailureGenerator");
        failureThread.start();
    }

    public void stopRandomFailureSimulation() {
        synchronized (failureSimulationLock) {
            failureRunning = false;
        }
        if (failureThread != null) {
            failureThread.interrupt();
            failureThread = null;
        }
    }

    public void repairToIntermittent(Device d) {
        if (d.getDeviceTypeName() == "TrafficLightController") {
            repairDevices(d);
            TrafficLightController tlc = (TrafficLightController) d;
            tlc.setForceIntermittent(true);
            // Also set individual lights to INTERMITTENT to reflect the state visually
            tlc.getIntersectionLights().get(0).changeState(TrafficLightState.INTERMITTENT);
            tlc.getIntersectionLights().get(1).changeState(TrafficLightState.INTERMITTENT);
        } else {
            repairDevices(d);
        }
    }
    public boolean isFatalError(Device device) {
        if (device == null) return false;

        if (device.getDeviceTypeName() == "TrafficLightController") {
            TrafficLightController tlc = (TrafficLightController) device;
            // A traffic light controller has a fatal error if either of its lights are UNKNOWN
            return tlc.getIntersectionLights().get(0).getCurrentState() == TrafficLightState.UNKNOWN ||
                    tlc.getIntersectionLights().get(1).getCurrentState() == TrafficLightState.UNKNOWN;
        }
        // Non-TrafficLight devices cannot have fatal errors, so return false for them.
        return false;
    }
    public void setFatalError(Device device) {
        if (device == null) return;

        // Only TrafficLightControllers can have fatal errors in the specific sense of UNKNOWN state.
        if (device.getDeviceTypeName() == "TrafficLightController") {
            TrafficLightController tlc = (TrafficLightController) device;
            // Check if it's already a fatal error to avoid redundant actions
            if (isFatalError(device)) {
                return;
            }
            tlc.getIntersectionLights().get(0).changeState(TrafficLightState.UNKNOWN);
            tlc.getIntersectionLights().get(1).changeState(TrafficLightState.UNKNOWN);
            // Add the controller's ID to the fatal error set
            if (this.fatalErrorDevices == null) {
                this.fatalErrorDevices = new HashSet<>();
            }
            this.fatalErrorDevices.add(device.getId());
            // The fatal error state is handled by setting lights to UNKNOWN and adding the specific fatal error record.
            failureRecords.add(new FailureRecord(device, "ERROR FATAL")); // Changed message here
        } else {
            // For non-TrafficLight devices, we treat any failure as making them 'NO OPERATIVO'.
            // They don't have a 'fatal error' state like traffic lights, but they can still fail.
            if (device.getState()) { // Only if it's currently operative, to avoid redundant records
                device.setState(false);
                failureRecords.add(new FailureRecord(device, "NO OPERATIVO"));
            }
        }
        UrbanMonitoringCenter.getUrbanMonitoringCenter().saveDevices("devices.ser");
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
        long cumulativeDelay = 0;
        long stepDelay = 500; // The delay between the start of each traffic light

        for (TrafficLightController tlc : trafficLights) {
            tlc.setInitialDelay(cumulativeDelay);
            new Thread(tlc).start();
            cumulativeDelay += stepDelay;
        }
    }

    public Set<Fine> getAllFinesFromDevice(String deviceAddress) throws SQLException {
        Device targetDevice = null;
        for (Device d : devices.values()) {
            if (d.getLocation() != null &&
                    d.getAddress().equalsIgnoreCase(deviceAddress)) {
                targetDevice = d;
                break;
            }
        }

        if (targetDevice == null) {
            System.out.println("No se encontró un dispositivo con la dirección: " + deviceAddress);
            return Collections.emptySet();
        }

        FineDAO finesDao = new FineDAO();

        Timestamp start = Timestamp.valueOf("2000-01-01 00:00:00");
        Timestamp end = new Timestamp(System.currentTimeMillis());

        Set<Fine> fines = finesDao.getNFinesByDeviceUUIDBetweenDates(start, end, Integer.MAX_VALUE, 0, targetDevice.getId());

        return fines;
    }
}