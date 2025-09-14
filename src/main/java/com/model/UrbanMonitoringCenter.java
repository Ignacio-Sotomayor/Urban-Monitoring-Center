package com.model;
import com.model.Automobile.MotorVehicleRegistry;
import com.model.Devices.*;
import com.model.Fines.ExcessiveSpeed;
import com.model.Fines.InfractionType;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class UrbanMonitoringCenter {
    private static Map<UUID,Device> workingDevices;
    private static Map<UUID,Device> issuedDevices;
    private Set<SecurityNotice> securityNotices;
    private Map<String,InfractionType> infractionTypes;

    private static UrbanMonitoringCenter instance=null;

    private UrbanMonitoringCenter(){
        workingDevices = new HashMap<>();
        issuedDevices = new HashMap<>();
        securityNotices = new TreeSet<>();
        infractionTypes = new HashMap<>();
    }

    public void addSecurityNotice(SecurityNotice notice){ securityNotices.add(notice); }

    //getters
    public static UrbanMonitoringCenter getUrbanMonitoringCenter(){
        if(instance == null)
            instance = new UrbanMonitoringCenter();
        return instance;
    }
    public InfractionType getSpecificInfractionType(String key){ return infractionTypes.get(key); }
    public static Device getSpecificDevice(UUID uuid){

        Device device = workingDevices.get(uuid);
        if(device == null)
            device = issuedDevices.get(uuid);
        return device;
    }
    public List<Device> getWorkingDevices() { return workingDevices.values().stream().toList(); }
    public Iterator<SecurityNotice> getSecurityNotices() { return securityNotices.iterator(); }

    public Device getRandomDevice(){
        Random random = new Random();
        return workingDevices.get(random.nextInt(workingDevices.size()));
    }
    public FineIssuerDevice getRandomFineIssuerDevice() {
        List<FineIssuerDevice> fineIssuers = workingDevices.values().stream()
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
        if(! workingDevices.isEmpty() && workingDevices.values().contains(d)) {
            workingDevices.remove(d);
            issuedDevices.put(d.getId(),d);
        }
    }
    public void repairDevices(Device d){
        if(! issuedDevices.isEmpty() && issuedDevices.values().contains(d)){
            issuedDevices.remove(d);
            workingDevices.put(d.getId(),d);
        }
    }

    public void informAllSecurityNotices(){
        Iterator<SecurityNotice> it = securityNotices.iterator();
        while(it.hasNext()){
            SecurityNotice actual = it.next();
            System.out.format("A %s ocurred in %s at the %s", actual.getDescription(), actual.getEventGeolocation().getAddress(), actual.getEventGeolocation().getDateTime().format(DateTimeFormatter.ISO_DATE_TIME));
        }
    }

    public static void Initialize(){
        UrbanMonitoringCenter UMC = getUrbanMonitoringCenter();
        //InfractionType
        UMC.infractionTypes.put("RedLightViolation", new InfractionType("RedLightViolation","The automobile was captured crossing a red Light",BigDecimal.valueOf(141600),5));
        UMC.infractionTypes.put("ExcessiveSpeed", new ExcessiveSpeed("The automobile was captured driving over the speed limit",BigDecimal.valueOf(217800),2, BigDecimal.valueOf(20)));
        UMC.infractionTypes.put("ParkingOverTime", new InfractionType("ParkingOverTime","The automobile was captured parking in a forbidden place for too much time",BigDecimal.valueOf(70800),3));
        //Vehicles
        MotorVehicleRegistry.Initialize();

        //TrafficLightControllers in Av Independecia
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 2500",new Location(BigDecimal.valueOf(-38.002863),BigDecimal.valueOf(-57.558199)),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Gascon",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 2400",new Location(BigDecimal.valueOf(-38.002053),BigDecimal.valueOf(-57.557535)),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Falucho",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 2300",new Location(BigDecimal.valueOf(-38.001261),BigDecimal.valueOf(-57.556904)),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Almirante Brown",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia, Av. Colon",new Location(BigDecimal.valueOf(-38.000394),BigDecimal.valueOf(-57.556213)),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Av. Colon",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 2100",new Location(BigDecimal.valueOf(-37.999542),BigDecimal.valueOf(-57.555559)),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Bolivar",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 2000",new Location(BigDecimal.valueOf(-37.998758),BigDecimal.valueOf(-57.554895)),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Moreno",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 1900",new Location(BigDecimal.valueOf(-37.997960),BigDecimal.valueOf(-57.554263)),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Belgrano",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 1800",new Location(BigDecimal.valueOf(-37.997194),BigDecimal.valueOf(-57.553623)),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Rivadavia",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 1700",new Location(BigDecimal.valueOf(-37.996395),BigDecimal.valueOf(-57.552932)),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "San Martin",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia, Av. Pedro Luro",new Location(BigDecimal.valueOf(-37.995545),BigDecimal.valueOf(-57.552259)),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Av. Pedro Luro",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 1500",new Location(BigDecimal.valueOf(-37.994681),BigDecimal.valueOf(-57.551600)),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "25 de Mayo",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 1400",new Location(BigDecimal.valueOf(-37.993883),BigDecimal.valueOf(-57.550930)),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "9 de Julio",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 1300",new Location(BigDecimal.valueOf(-37.993094),BigDecimal.valueOf(-57.550322)),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "3 de Febrero",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 1200",new Location(BigDecimal.valueOf(-37.992316),BigDecimal.valueOf(-57.549679)),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "11 de Septiembre",false)));

        //TrafficLightControllers in Rivadavia street
        UMC.workingDevices.add(new TrafficLightController("Rivadavia 2900",new Location(BigDecimal.valueOf(-37.998781),BigDecimal.valueOf(-57.550557)),new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Hipolito Yrigoyen",false)));
        UMC.workingDevices.add(new TrafficLightController("Rivadavia 2800",new Location(BigDecimal.valueOf(-37.999231),BigDecimal.valueOf(-57.549566)),new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Bartolome Mitre",false)));
        UMC.workingDevices.add(new TrafficLightController("Rivadavia 2700",new Location(BigDecimal.valueOf(-37.999741),BigDecimal.valueOf(-57.548524)),new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "San Luis",false)));
        UMC.workingDevices.add(new TrafficLightController("Rivadavia 2600",new Location(BigDecimal.valueOf(-38.000255),BigDecimal.valueOf(-57.547554)),new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Cordoba",false)));

        //Radars
        UMC.workingDevices.add(new Radar("Av. Pedro Luro 3242",new Location(BigDecimal.valueOf(-37.995296),BigDecimal.valueOf(-57.552912)),60));
        UMC.workingDevices.add(new Radar("Av. Pedro Luro 2556",new Location(BigDecimal.valueOf(-37.998900),BigDecimal.valueOf(-57.545851)),60));

        //Security Cameras
        UMC.workingDevices.add(new SecurityCamera("Peatonal San Martin 2802",new Location(BigDecimal.valueOf(-37.998457),BigDecimal.valueOf(-57.549009))));
        UMC.workingDevices.add(new SecurityCamera("25 de Mayo 2700",new Location(BigDecimal.valueOf(-37.997282),BigDecimal.valueOf(-57.54628))));

        //ParkingLotSecurityCameras
        UMC.workingDevices.add(new ParkingLotSecurityCamera("Av. Colon 2900", new Location(BigDecimal.valueOf(-38.002099),BigDecimal.valueOf(-57.553031)), Duration.ofSeconds(1200)));
    }
}