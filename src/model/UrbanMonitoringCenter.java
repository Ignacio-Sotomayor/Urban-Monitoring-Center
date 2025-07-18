package model;
import model.Automobile.MotorVehicleRegistry;
import model.Devices.*;
import model.Fines.ExcessiveSpeed;
import model.Fines.InfractionType;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UrbanMonitoringCenter {
    private List<Device> devices;
    private Set<SecurityNotice> securityNotices;
    private Map<String,InfractionType> infractionTypes;

    private static UrbanMonitoringCenter instance=null;

    private UrbanMonitoringCenter(){
        devices = new ArrayList<>();
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
    public List<Device> getDevices() { return devices; }
    public Iterator<SecurityNotice> getSecurityNotices() { return securityNotices.iterator(); }
    public Device getRandomDevice(){
        Random random = new Random();
        return devices.get(random.nextInt(devices.size()));
    }
    public FineIssuerDevice getRandomFineIssuerDevice(){
        Device device;
        do{
            device = getRandomDevice();
        }while(device.getClass()!= FineIssuerDevice.class);
        return (FineIssuerDevice)device;
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
        UMC.devices.add(new TrafficLightController("Av. Independecia 2500",new Location(-38.002863,-57.558199),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Gascon",false)));
        UMC.devices.add(new TrafficLightController("Av. Independecia 2400",new Location(-38.002053,-57.557535),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Falucho",false)));
        UMC.devices.add(new TrafficLightController("Av. Independecia 2300",new Location(-38.001261,-57.556904),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Almirante Brown",false)));
        UMC.devices.add(new TrafficLightController("Av. Independecia, Av. Colon",new Location(-38.000394,-57.556213),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Av. Colon",false)));
        UMC.devices.add(new TrafficLightController("Av. Independecia 2100",new Location(-37.999542,-57.555559),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Bolivar",false)));
        UMC.devices.add(new TrafficLightController("Av. Independecia 2000",new Location(-37.998758,-57.554895),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Moreno",false)));
        UMC.devices.add(new TrafficLightController("Av. Independecia 1900",new Location(-37.997960,-57.554263),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Belgrano",false)));
        UMC.devices.add(new TrafficLightController("Av. Independecia 1800",new Location(-37.997194,-57.553623),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Rivadavia",false)));
        UMC.devices.add(new TrafficLightController("Av. Independecia 1700",new Location(-37.996395,-57.552932),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "San Martin",false)));
        UMC.devices.add(new TrafficLightController("Av. Independecia, Av. Pedro Luro",new Location(-37.995545,-57.552259),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "Av. Pedro Luro",false)));
        UMC.devices.add(new TrafficLightController("Av. Independecia 1500",new Location(-37.994681,-57.551600),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "25 de Mayo",false)));
        UMC.devices.add(new TrafficLightController("Av. Independecia 1400",new Location(-37.993883,-57.550930),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "9 de Julio",false)));
        UMC.devices.add(new TrafficLightController("Av. Independecia 1300",new Location(-37.993094,-57.550322),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "3 de Febrero",false)));
        UMC.devices.add(new TrafficLightController("Av. Independecia 1200",new Location(-37.992316,-57.549679),new TrafficLight("Av. Independecia", "Av. Indepencia", true), new TrafficLight("Av. Independecia", "11 de Septiembre",false)));

        //TrafficLightControllers in Rivadavia street
        UMC.devices.add(new TrafficLightController("Rivadavia 2900",new Location(-37.998781,-57.550557),new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Hipolito Yrigoyen",false)));
        UMC.devices.add(new TrafficLightController("Rivadavia 2800",new Location(-37.999231,-57.549566),new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Bartolome Mitre",false)));
        UMC.devices.add(new TrafficLightController("Rivadavia 2700",new Location(-37.999741,-57.548524),new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "San Luis",false)));
        UMC.devices.add(new TrafficLightController("Rivadavia 2600",new Location(-38.000255,-57.547554),new TrafficLight("Rivadavia", "Rivadavia", true), new TrafficLight("Rivadavia", "Cordoba",false)));

        //Radars
        UMC.devices.add(new Radar("Av. Pedro Luro 3242",new Location(-37.995296,-57.552912),60));
        UMC.devices.add(new Radar("Av. Pedro Luro 2556",new Location(-37.998900,-57.545851),60));

        //Security Cameras
        UMC.devices.add(new SecurityCamera("Peatonal San Martin 2802",new Location(-37.998457,-57.549009)));
        UMC.devices.add(new SecurityCamera("25 de Mayo 2700",new Location(-37.997282,-57.54628)));

        //ParkingLotSecurityCameras
        UMC.devices.add(new ParkingLotSecurityCamera("Av. Colon 2900", new Location(-38.002099,-57.553031), Duration.ofSeconds(1200)));
    }
}