package com.controller;
import com.DAO.FineDAO;
import com.DAO.InfractionTypesDAO;
import com.DAO.OwnersDAO;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfWriter;
import com.model.Automobile.Automobile;
import com.model.Automobile.MotorVehicleRegistry;
import com.model.Devices.*;
import com.model.Fines.Fine;
import com.model.Fines.InfractionType;
import com.model.SecurityNotice;


import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private Set<UUID> fatalErrorDevices;
    private Map<String,InfractionType> infractionTypes;
    private Queue<FailureRecord> failureRecords;

    private static UrbanMonitoringCenter instance=null;

    private Thread fineThread;
    private volatile boolean running = false;

    private static final int MIN_FINE_INTERVAL = 5;   // en segundos
    private static final int MAX_FINE_INTERVAL = 15;  // en segundos

    private UrbanMonitoringCenter(){
        devices = new HashMap<>();
        securityNotices = new TreeSet<>();
        infractionTypes = new HashMap<>();
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

    public Device getRandomDevice(){
        Random random = new Random();
        return devices.get(random.nextInt(devices.size()));
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
        return fineIssuers.get(random.nextInt(fineIssuers.size()));
    }
    public void issuedDevices(Device d){
        if(! devices.isEmpty() && devices.containsValue(d)) {
            d.setState(false);
        }
    }
    public void repairDevices(Device d){
        if(! devices.isEmpty() && devices.containsValue(d)){
            d.setState(true);
        }
    }

   /* public void informAllSecurityNotices(){
        SecurityNoticeDAO dao = new SecurityNoticeDAO();
        Iterator<SecurityNotice> it = dao;
        while(it.hasNext()){
            SecurityNotice actual = it.next();
            System.out.format("A %s ocurred in %s at the %s", actual.getDescription(), actual.getEventGeolocation().getAddress(), actual.getEventGeolocation().getDateTime().format(DateTimeFormatter.ISO_DATE_TIME));
        }
    } */


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

        //TrafficLightControllers in Av Independecia
        tf = new TrafficLightController("Av. Independecia 2500",new Location(BigDecimal.valueOf(-38.002863),BigDecimal.valueOf(-57.558199)),true, new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Gascon",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 2400",new Location(BigDecimal.valueOf(-38.002053),BigDecimal.valueOf(-57.557535)),true,new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Falucho",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 2300",new Location(BigDecimal.valueOf(-38.001261),BigDecimal.valueOf(-57.556904)),true,new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Almirante Brown",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia, Av. Colon",new Location(BigDecimal.valueOf(-38.000394),BigDecimal.valueOf(-57.556213)),true,new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Av. Colon",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 2100",new Location(BigDecimal.valueOf(-37.999542),BigDecimal.valueOf(-57.555559)),true,new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Bolivar",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 2000",new Location(BigDecimal.valueOf(-37.998758),BigDecimal.valueOf(-57.554895)),true,new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Moreno",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 1900",new Location(BigDecimal.valueOf(-37.997960),BigDecimal.valueOf(-57.554263)),true,new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Belgrano",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 1800",new Location(BigDecimal.valueOf(-37.997194),BigDecimal.valueOf(-57.553623)),true,new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Rivadavia",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 1700",new Location(BigDecimal.valueOf(-37.996395),BigDecimal.valueOf(-57.552932)),true,new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "San Martin",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia, Av. Pedro Luro",new Location(BigDecimal.valueOf(-37.995545),BigDecimal.valueOf(-57.552259)),true,new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Av. Pedro Luro",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 1500",new Location(BigDecimal.valueOf(-37.994681),BigDecimal.valueOf(-57.551600)),true,new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "25 de Mayo",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 1400",new Location(BigDecimal.valueOf(-37.993883),BigDecimal.valueOf(-57.550930)),true,new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "9 de Julio",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 1300",new Location(BigDecimal.valueOf(-37.993094),BigDecimal.valueOf(-57.550322)),true,new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "3 de Febrero",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 1200",new Location(BigDecimal.valueOf(-37.992316),BigDecimal.valueOf(-57.549679)),true,new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "11 de Septiembre",false));
        UMC.devices.put(tf.getId(),tf);

        //TrafficLightControllers in Rivadavia street
        tf = new TrafficLightController("Rivadavia 2900",new Location(BigDecimal.valueOf(-37.998781),BigDecimal.valueOf(-57.550557)),true,new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Hipolito Yrigoyen",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Rivadavia 2800",new Location(BigDecimal.valueOf(-37.999231),BigDecimal.valueOf(-57.549566)),true,new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Bartolome Mitre",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Rivadavia 2700",new Location(BigDecimal.valueOf(-37.999741),BigDecimal.valueOf(-57.548524)),true,new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "San Luis",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Rivadavia 2600",new Location(BigDecimal.valueOf(-38.000255),BigDecimal.valueOf(-57.547554)),true,new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Cordoba",false));
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

        ParkingLotSecurityCamera plCamera;
        //ParkingLotSecurityCameras
        plCamera = new ParkingLotSecurityCamera("Av. Colon 2900", new Location(BigDecimal.valueOf(-38.002099),BigDecimal.valueOf(-57.553031)),true, Duration.ofSeconds(1200));
        UMC.devices.put(plCamera.getId(),plCamera);

        UMC.saveDevices("devices.ser");
        //UMC.startSimulations();
    }
    public static void lastStateStart(){
        UrbanMonitoringCenter UMC = getUrbanMonitoringCenter();
        UMC.loadDevices("devices.ser");
        UMC.loadInfractionTypes();
        //UMC.startSimulations();
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
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadInfractionTypes() {
        InfractionTypesDAO dao = new InfractionTypesDAO();
        try {
            List<InfractionType> types = dao.getAllInfractionTypes();
            for (InfractionType t : types) {
                infractionTypes.put(t.getName(), t);
            }
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

    public synchronized void startRandomFineSimulation() {
        stopRandomFineSimulation();

        running = true;
        fineThread = new Thread(() -> {
            Random random = new Random();

            while (running) {
                try {
                    simulateRandomFineOnce();
                    int delay = random.nextInt(MAX_FINE_INTERVAL - MIN_FINE_INTERVAL + 1) + MIN_FINE_INTERVAL;
                    Thread.sleep(delay * 1000);

                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        fineThread.setDaemon(true);
        fineThread.setName("RandomFineGenerator");
        fineThread.start();
    }
    public synchronized void stopRandomFineSimulation() {
        running = false;
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
            System.out.println(device.getEmitedInfractionType().getName());
        } catch (IllegalStateException e) {
            System.err.println("No hay dispositivos emisores disponibles.");
            return;
        }

        Automobile auto = mvr.getRandomAutomobile();
        if (auto == null) {
            System.err.println("No hay autos disponibles.");
            return;
        }

        if (!device.getState()) {
            System.out.println("Dispositivo inoperativo, se salta: " + device.getId());
            return;
        }

        try {
            device.issueFine(auto);
            System.out.println("Multa emitida por " + device.getAddress() + " a " + auto.getLicensePlate());
        } catch (RuntimeException e) {
            System.err.println("Error al emitir multa: " + e.getMessage());
        }
    }

    //revisar y arreglar
    public void repairToIntermittent(Device d) {
        if (d instanceof TrafficLightController) {
            repairDevices(d);
            ((TrafficLightController) d).setForceIntermittent(true);
        }
    }
    public boolean isFatalError(Device device) {
        if (device == null) return false;
        if (device instanceof TrafficLightController) {
            return ((TrafficLightController) device).getIntersectionLights().get(0).getCurrentState() == TrafficLightState.UNKNOWN;
        }
        return fatalErrorDevices!=null && this.fatalErrorDevices.contains(device.getId());
    }
    public void setFatalError(Device device) {
        if (device != null && !isFatalError(device)) {
            if (device instanceof TrafficLightController) {
                ((TrafficLightController) device).changeTrafficLights(TrafficLightState.UNKNOWN, TrafficLightState.UNKNOWN);
            } else {
                this.fatalErrorDevices.add(device.getId());
            }
            issuedDevices(device);
            failureRecords.add(new FailureRecord(device, "ERROR FATAL"));
        }
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
                new Thread(tlc).start();
                Thread.sleep(stepDelay);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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