package com.model;
import com.DAO.InfractionTypesDAO;
import com.DAO.OwnersDAO;
import com.DAO.SecurityNoticeDAO;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfWriter;
import com.model.Automobile.Automobile;
import com.model.Automobile.MotorVehicleRegistry;
import com.model.Devices.*;
import com.model.Fines.ExcessiveSpeed;
import com.model.Fines.Fine;
import com.model.Fines.InfractionType;


import java.io.*;
import java.math.BigDecimal;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class UrbanMonitoringCenter {
    private HashMap<UUID,Device> devices;
    private Set<SecurityNotice> securityNotices;
    private Map<String,InfractionType> infractionTypes;

    private static UrbanMonitoringCenter instance=null;

    private Thread fineThread;
    private volatile boolean running = false;

    private static final int MIN_FINE_INTERVAL = 3;   // en segundos
    private static final int MAX_FINE_INTERVAL = 7;  // en segundos

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

    public static void loadDevices(){
        UrbanMonitoringCenter UMC = getUrbanMonitoringCenter();
        InfractionType speeding = UMC.getSpecificInfractionType("Speeding");
        InfractionType redLightViolation = UMC.getSpecificInfractionType("RedLightViolation");
        InfractionType parkingOverTime = UMC.getSpecificInfractionType("ParkingOverTime");

        TrafficLightController tf;

        //TrafficLightControllers in Av Independecia
        tf = new TrafficLightController("Av. Independecia 2500",new Location(BigDecimal.valueOf(-38.002863),BigDecimal.valueOf(-57.558199)),true, redLightViolation,new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Gascon",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 2400",new Location(BigDecimal.valueOf(-38.002053),BigDecimal.valueOf(-57.557535)),true, redLightViolation, new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Falucho",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 2300",new Location(BigDecimal.valueOf(-38.001261),BigDecimal.valueOf(-57.556904)),true, redLightViolation, new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Almirante Brown",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia, Av. Colon",new Location(BigDecimal.valueOf(-38.000394),BigDecimal.valueOf(-57.556213)),true, redLightViolation, new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Av. Colon",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 2100",new Location(BigDecimal.valueOf(-37.999542),BigDecimal.valueOf(-57.555559)),true, redLightViolation, new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Bolivar",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 2000",new Location(BigDecimal.valueOf(-37.998758),BigDecimal.valueOf(-57.554895)),true, redLightViolation, new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Moreno",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 1900",new Location(BigDecimal.valueOf(-37.997960),BigDecimal.valueOf(-57.554263)),true, redLightViolation, new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Belgrano",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 1800",new Location(BigDecimal.valueOf(-37.997194),BigDecimal.valueOf(-57.553623)),true, redLightViolation, new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Rivadavia",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 1700",new Location(BigDecimal.valueOf(-37.996395),BigDecimal.valueOf(-57.552932)),true, redLightViolation, new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "San Martin",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia, Av. Pedro Luro",new Location(BigDecimal.valueOf(-37.995545),BigDecimal.valueOf(-57.552259)),true, redLightViolation, new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Av. Pedro Luro",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 1500",new Location(BigDecimal.valueOf(-37.994681),BigDecimal.valueOf(-57.551600)),true, redLightViolation, new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "25 de Mayo",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 1400",new Location(BigDecimal.valueOf(-37.993883),BigDecimal.valueOf(-57.550930)),true, redLightViolation, new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "9 de Julio",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 1300",new Location(BigDecimal.valueOf(-37.993094),BigDecimal.valueOf(-57.550322)),true, redLightViolation, new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "3 de Febrero",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Av. Independecia 1200",new Location(BigDecimal.valueOf(-37.992316),BigDecimal.valueOf(-57.549679)),true, redLightViolation, new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "11 de Septiembre",false));
        UMC.devices.put(tf.getId(),tf);

        //TrafficLightControllers in Rivadavia street
        tf = new TrafficLightController("Rivadavia 2900",new Location(BigDecimal.valueOf(-37.998781),BigDecimal.valueOf(-57.550557)),true, redLightViolation, new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Hipolito Yrigoyen",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Rivadavia 2800",new Location(BigDecimal.valueOf(-37.999231),BigDecimal.valueOf(-57.549566)),true, redLightViolation, new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Bartolome Mitre",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Rivadavia 2700",new Location(BigDecimal.valueOf(-37.999741),BigDecimal.valueOf(-57.548524)),true, redLightViolation, new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "San Luis",false));
        UMC.devices.put(tf.getId(),tf);
        tf = new TrafficLightController("Rivadavia 2600",new Location(BigDecimal.valueOf(-38.000255),BigDecimal.valueOf(-57.547554)),true, redLightViolation, new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Cordoba",false));
        UMC.devices.put(tf.getId(),tf);

        Radar rd;
        //Radars
        rd = new Radar("Av. Pedro Luro 3242",new Location(BigDecimal.valueOf(-37.995296),BigDecimal.valueOf(-57.552912)),true, speeding,60);
        UMC.devices.put(rd.getId(),rd);
        rd = new Radar("Av. Pedro Luro 2556",new Location(BigDecimal.valueOf(-37.998900),BigDecimal.valueOf(-57.545851)),true, speeding,60);
        UMC.devices.put(rd.getId(),rd);

        SecurityCamera secCamera;
        //Security Cameras
        secCamera = new SecurityCamera("Peatonal San Martin 2802",new Location(BigDecimal.valueOf(-37.998457),BigDecimal.valueOf(-57.549009)),true);
        UMC.devices.put(secCamera.getId(),secCamera);
        secCamera = new SecurityCamera("25 de Mayo 2700",new Location(BigDecimal.valueOf(-37.997282),BigDecimal.valueOf(-57.54628)),true);
        UMC.devices.put(secCamera.getId(),secCamera);

        ParkingLotSecurityCamera plCamera;
        //ParkingLotSecurityCameras
        plCamera = new ParkingLotSecurityCamera("Av. Colon 2900", new Location(BigDecimal.valueOf(-38.002099),BigDecimal.valueOf(-57.553031)),true, parkingOverTime, Duration.ofSeconds(1200));
        UMC.devices.put(plCamera.getId(),plCamera);

        UMC.serializeAllDevices("devices.ser");
    }

    public static void Initialize(){
        UrbanMonitoringCenter UMC = getUrbanMonitoringCenter();
        UMC.loadInfractionTypes();
        UMC.loadDevices();
        // UMC.deserializeAllDevices("devices.ser");
    }

    public void serializeAllDevices(String fileName){
        try(ObjectOutputStream oop = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oop.writeObject(devices);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deserializeAllDevices(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            HashMap<UUID, Device> loadedDevices = (HashMap<UUID, Device>) ois.readObject();
            this.devices = loadedDevices;
            for (Device d : devices.values()) {
                if (d instanceof FineIssuerDevice fineDevice) {
                    fineDevice.setEmitedInfractionType(
                            this.getSpecificInfractionType(fineDevice.getDefaultInfractionTypeName())
                    );
                }
            }
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

    private String generateBarcode(int fineNumber, BigDecimal amount) {
        String finePart = String.format("%06d", fineNumber);

        BigDecimal scaled = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        String amountStr = scaled.toPlainString().replace(".", "");
        amountStr = String.format("%012d", Long.parseLong(amountStr));

        return finePart + amountStr;
    }

    
    public void generateFinePDF(Fine fine, int fineNumber) {
        Path outputDir = Paths.get("fines");
        try {
            if (!Files.exists(outputDir)) Files.createDirectories(outputDir);
        } catch (IOException e) {
            System.err.println("No se pudo crear carpeta fines: " + e.getMessage());
            return;
        }

        String filename = String.format("Multa_%06d.pdf", fineNumber);
        Path outputFile = outputDir.resolve(filename);

        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFile.toFile()));
            document.open();

            Paragraph header = new Paragraph("Dirección de Tránsito\n");
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Número de multa: " + String.format("%06d", fineNumber)));
            document.add(new Paragraph("Fecha de emisión: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Titular: " + fine.getAutomobile().getOwner().getFullName()));
            document.add(new Paragraph("DNI: " + fine.getAutomobile().getOwner().getLegalIid()));
            document.add(new Paragraph("Domicilio: " + fine.getAutomobile().getOwner().getAddress()));
            document.add(new Paragraph("Automóvil: " + fine.getAutomobile().getBrand() + " " + fine.getAutomobile().getModel()));
            document.add(new Paragraph("Patente: " + fine.getAutomobile().getLicensePlate()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Tipo de infracción: " + fine.getInfractionType().getDescription()));
            document.add(new Paragraph("Lugar: " + fine.getEventGeolocation().getAddress()));
            document.add(new Paragraph("Fecha/Hora: " + fine.getEventGeolocation().getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            document.add(new Paragraph("Valor a pagar: $" + fine.getAmount()));
            document.add(new Paragraph("Puntos a reducir: "));
            document.add(new Paragraph(" "));

            String barcodeValue = generateBarcode(fineNumber, fine.getAmount());
            document.add(new Paragraph("Código de barras: " + barcodeValue));

            Barcode128 barcode = new Barcode128();
            barcode.setCode(barcodeValue);
            Image barcodeImage = barcode.createImageWithBarcode(writer.getDirectContent(), null, null);
            barcodeImage.scalePercent(150);
            barcodeImage.setAlignment(Element.ALIGN_CENTER);
            document.add(barcodeImage);

            document.close();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}