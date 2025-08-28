package com.model;
import com.model.Automobile.MotorVehicleRegistry;
import com.model.Devices.*;
import com.model.Fines.ExcessiveSpeed;
import com.model.Fines.InfractionType;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class UrbanMonitoringCenter {
    private List<Device> workingDevices;
    private List<Device> issuedDevices;
    private Set<SecurityNotice> securityNotices;
    private Map<String,InfractionType> infractionTypes;

    private static UrbanMonitoringCenter instance=null;

    private UrbanMonitoringCenter(){
        workingDevices = new ArrayList<>();
        issuedDevices = new ArrayList<>();
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
    public List<Device> getWorkingDevices() { return workingDevices; }
    public Iterator<SecurityNotice> getSecurityNotices() { return securityNotices.iterator(); }

    public Device getRandomDevice(){
        Random random = new Random();
        return workingDevices.get(random.nextInt(workingDevices.size()));
    }
    public FineIssuerDevice getRandomFineIssuerDevice() {
        List<FineIssuerDevice> fineIssuers = workingDevices.stream()
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
        if(! workingDevices.isEmpty() && workingDevices.contains(d)) {
            workingDevices.remove(d);
            issuedDevices.add(d);
        }
    }
    public void repairDevices(Device d){
        if(! issuedDevices.isEmpty() && issuedDevices.contains(d)){
            issuedDevices.remove(d);
            workingDevices.add(d);
        }
    }

    public void informAllSecurityNotices(){
        Iterator<SecurityNotice> it = securityNotices.iterator();
        while(it.hasNext()){
            SecurityNotice actual = it.next();
            System.out.format("A %s ocurred in %s at the %s", actual.getDescription(), actual.getEventGeolocation().getAddress(), actual.getEventGeolocation().getDateHour().format(DateTimeFormatter.ISO_DATE_TIME));
        }
    }

    public static void Initialize(){
        UrbanMonitoringCenter UMC = getUrbanMonitoringCenter();
        //InfractionType
        UMC.infractionTypes.put("CrossingRedLight", new InfractionType("The automobile was captured crossing a red Light",141600,5));
        UMC.infractionTypes.put("ExcessiveSpeed", new ExcessiveSpeed("The automobile was captured driving over the speed limit",217800,2,20));
        UMC.infractionTypes.put("ParkingOvertime", new InfractionType("The automobile was captured parking in a forbidden place for too much time",70800,3));
        //Vehicles
        MotorVehicleRegistry.Initialize();

        //TrafficLightControllers in Av Independecia
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 2500",new Location(-38.002863,-57.558199),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Gascon",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 2400",new Location(-38.002053,-57.557535),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Falucho",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 2300",new Location(-38.001261,-57.556904),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Almirante Brown",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia, Av. Colon",new Location(-38.000394,-57.556213),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Av. Colon",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 2100",new Location(-37.999542,-57.555559),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Bolivar",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 2000",new Location(-37.998758,-57.554895),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Moreno",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 1900",new Location(-37.997960,-57.554263),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Belgrano",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 1800",new Location(-37.997194,-57.553623),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Rivadavia",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 1700",new Location(-37.996395,-57.552932),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "San Martin",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia, Av. Pedro Luro",new Location(-37.995545,-57.552259),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Av. Pedro Luro",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 1500",new Location(-37.994681,-57.551600),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "25 de Mayo",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 1400",new Location(-37.993883,-57.550930),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "9 de Julio",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 1300",new Location(-37.993094,-57.550322),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "3 de Febrero",false)));
        UMC.workingDevices.add(new TrafficLightController("Av. Independecia 1200",new Location(-37.992316,-57.549679),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "11 de Septiembre",false)));

        //TrafficLightControllers in Rivadavia street
        UMC.workingDevices.add(new TrafficLightController("Rivadavia 2900",new Location(-37.998781,-57.550557),new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Hipolito Yrigoyen",false)));
        UMC.workingDevices.add(new TrafficLightController("Rivadavia 2800",new Location(-37.999231,-57.549566),new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Bartolome Mitre",false)));
        UMC.workingDevices.add(new TrafficLightController("Rivadavia 2700",new Location(-37.999741,-57.548524),new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "San Luis",false)));
        UMC.workingDevices.add(new TrafficLightController("Rivadavia 2600",new Location(-38.000255,-57.547554),new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Cordoba",false)));

        //Radars
        UMC.workingDevices.add(new Radar("Av. Pedro Luro 3242",new Location(-37.995296,-57.552912),60));
        UMC.workingDevices.add(new Radar("Av. Pedro Luro 2556",new Location(-37.998900,-57.545851),60));

        //Security Cameras
        UMC.workingDevices.add(new SecurityCamera("Peatonal San Martin 2802",new Location(-37.998457,-57.549009)));
        UMC.workingDevices.add(new SecurityCamera("25 de Mayo 2700",new Location(-37.997282,-57.54628)));

        //ParkingLotSecurityCameras
        UMC.workingDevices.add(new ParkingLotSecurityCamera("Av. Colon 2900", new Location(-38.002099,-57.553031), Duration.ofSeconds(1200)));
    }
}