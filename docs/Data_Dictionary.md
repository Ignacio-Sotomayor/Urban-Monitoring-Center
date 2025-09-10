# Urban Monitoring Center - Data Dictionary

## *Automobile Knowledge Area*
#### Class Person
Represents an individual who owns a vehicle registered in the `Motor Vehicle Registry`.
- **Fields:**
  - `fullName`: String - Stores the full name of the car owner.
  - `id`: String - Unique identifier for each owner. In Argentina, this conveniently corresponds to the national identity document (DNI).
  - `address`: String - Specifies the residential address of the vehicle owner.
-----
#### Class Brand
Represents an automobile manufacturer whose vehicles are registered in the `Motor Vehicle Registry`.
- **Fields:**
  - `name`: String - Stores the name of the brand (e.g., "Toyota", "Ford").
  - `models`: Set<Model> – Contains all car models associated with this brand.
-----
#### Class Model
Represents the specific model of a car, distinguishing variations within a brand.
- **Fields:**
  - `name`: String - Stores the name of the car model (e.g., "Corolla", "Mustang", "Civic").
-----
#### Class Automobile
Encapsulates the data of a vehicle listed in the `Motor Vehicle Registry`.
- **Fields:**
  - `licensePlate`: String - Represents the license plate of the vehicle. Acts as a unique identifier for each car within the registry.
  - `owner`: Person - Links the vehicle to its registered owner.
  - `brand`: Brand - Links the vehicle to its brand information.
  - `model`: Model - Links the vehicle to its model information.
  - `year`: int - Represents the year the vehicle was manufactured. Useful for distinguishing between model years and tracking age-related attributes.
-----
#### Class Motor Vehicle Registry
Singleton class. Stores all registered automobile data, brands, and fines for a specific geographic or administrative area.
- **Fields:**
  - `name`: String - Specify the name of the Motor Vehicle Registry.
  - `brands`: Set<Brand> - Contains automobile manufacturer data.
  - `automobiles`: List<Automobile> - Holds information for all registered vehicles.
  - `finesMap`: Map<String, List<Fine> > - Maps each license plate (String) to its associated list of traffic fines.
-----
## *Fines Knowledge Area*
#### Class Photo
Stores the logical location of a photo, used for documenting vehicle fines.
- **Fields:**
  - `path`: String - Specifies the logical file path or URI where the photo is stored.
-----
#### Abstract Class InfractionType
Encapsulates the general attributes of an infraction that results in a fine. Intended for extension by specific types of traffic or regulatory violations.
- **Fields:**
  - `description`: String - Provides a brief summary of the infraction (e.g., "Speeding over limit").
  - `amount`: double "- Provides a brief summary of the infraction (e.g., "Speeding over limit, "Running a red light").
  - `scoring`: int - Indicates the number of points deducted from the owner's driving record.
-----
#### Class ExcessiveSpeed
Inherits from `InfractionType`. Encapsulates specific data related to fines issued for exceeding legal speed limits.
- **Fields:**
  - `surchagePer10PercentExcess`: double - Specifies the additional penalty applied for every 10% the vehicle exceeds the posted speed limit.
-----
#### Class EventGeolocation
Captures the spatiotemporal details of an event within the system.
- **Fields:**
  - `id`: String - Unique identifier for each event.
  - `dateHour`: DateTime - Records the exact timestamp when the event occurred, formatted as dd/MM/yyyy HH:mm:ss.
  - `address`: String - Stores the street name and number of the event location.
  - `location`: Location - Provides precise geographic coordinates where the event took place.
-----
#### Class Fine
Encapsulates information about a traffic regulation violation associated with a specific automobile.
- **Fields:**
  - `amount`: double - Specifies the monetary penalty associated with the infraction.
  - `scoring`: int - Indicates the number of points deducted from the driver’s record due to the violation.
  - `photos`: Set<Photo> - Contains all photographic evidence related to the infraction.
  - `eventGeolocation`: EventGeolocation - Provides detailed information about the time and location where the infraction occurred.
  - `infractionType`: InfractionType - Describes the category or nature of the violation that triggered the fine.
  - `automobile`: Automobile - Identifies the vehicle responsible for committing the infraction.
-----
#### Class ExcessiveSpeedFine
Inherits from `Fine`. Encapsulates additional data specific to violations involving excessive speed beyond posted legal limits.
- **Fields:**
  - `automobileSpeed`: int - Captures the vehicle’s actual speed at the moment of the infraction.
  - `speedLimit`: int - Indicates the posted maximum speed allowed at the infraction location.
---
## *Device Knowledge Area*
#### Class Location
Encapsulates precise geographic coordinates data for spatial reference within the system.
- **Fields:**
  - `latitude`: double - Specify the latitude of the location.
  - `longitude`: double - Specify the longitude of the location.
-----
#### Abstract Class Device
Encapsulates the general attributes of all the devices that conform the system. Intend for extension by specific types of devices.
- **Fields:**
  - `id`: int - Unique indentifier for each device.
  - `address`: String - Stores the street name and number where the device is physically installed.
  - `state`: State - Indicates the operational status of the device
  - `location`: Location - Provides precise geographic coordinates for the device’s placement.
  - `infractionEmited`: @Nullable InfractionType - Indicates the type of infraction this device is capable of issuing, if applicable. When null, the device does not issue fines directly.
-----
#### Interface FineIssuer
Defines the contract for any class that has the capability to issue fines.
- **Methods:**
  - `issueFine()`: void - Triggers the logic required to generate and record a fine based on the issuing entity's context and captured data.
-----
#### Class SecurityCamera
  - Inherits from `Device`. Represents a fixed-position camera that streams live video from its installed location.
----
#### Class ParkingLotSecurityCamera
Inherits from `Device`. Implemments `FineIssuer`. Monitors vehicle parking durations and issues fines for overtime violations.
- **Fields:**
  - `toleranceTime`: Duration - Indicates the maximum allowable time a vehicle may be parked before triggering a violation.
- **Methods:**
  - `issueFine()`: to develope.
-----
#### Class Radar
Inherits from `Device`. Implements `FineIssuer`. Enforces vehicle speed compliance by monitoring and issuing fines when speed limits are exceeded.
- **Fields:**
  - `speedLimit`: double - Indicates the maximum allowable speed at the radar’s location.
- **Methods:**
  - `issueFine()`: void - Triggers fine issuance logic. Implementation should analyze captured vehicle data and generate a fine if the speed exceeds the speedLimit.
-----
#### Class TrafficLight
Inherits from `Device`. Controls vehicle crossing flow at a specific intersection based on signal states.
- **Fields:**
  - `street`: String - Specifies the street where the traffic light is installed.
  - `orientation`:String - Indicates the direction or intersecting street the traffic light faces.
  - `isMain`: boolean - Flags whether this traffic light is the primary signal within its intersection controller group.
  - `currentState`: TrafficLightState - Represents the current light status
- **Methods:**
  - `changeState(TrafficLightState newState)`: void - Updates the light’s current state to the new specified value.
  - `fail()`: void - Forces the light into an UNKNOWN state to simulate system failure or trigger alerts. Useful for testing error handling and redundancy logic.
-----
#### Enumerator TrafficLightState
Encapsulates all possible operational states of a traffic light within the monitoring system.
- **Values:**
  - `RED` - Indicates that vehicles must stop; halts crossing flow.
  - `GREEN` - Signals that vehicles may proceed; allows crossing flow.
  - `INTERMITTENT` - Transitional flashing state used between `RED` and `GREEN` to reduce collision risk.
  - `UNKNOWN` - Represents a fault condition where the traffic light is not operating correctly or its state cannot be determined.
-----
#### Class TrafficLightController
Inherits from `Device`. Implements `FineIssuer` and `Runnable`. Regulates the proper operation of intersection traffic lights and autonomously issues fines to vehicles that violate signal compliance.
- **Fields:**
  - `intermittentStartTime`: @ Nullable LocalDateTime - Indicates the start time when both traffic lights enter an INTERMITTENT (flashing) state.
  - `intermittentEndTime`: @ Nullable LocalDateTime - Indicates the end time for the INTERMITTENT state across the intersection.
  - `redLightDuration`: Duration - Duration the lights remain in the RED state during a cycle.
  - `yellowLightDuration`: Duration -Duration for the INTERMITTENT state, acting as a caution phase before switching.
  - `greenLightDuration`: Duration - Duration the lights stay in the GREEN state to allow crossing.
  - `bothRedLightDuration`: Duration - Time both lights stay in RED simultaneously to prevent collisions at switch boundaries.
  - `intersection`: ArrayList<TrafficLight>(2) - Contains the pair of traffic lights in the intersection. mainLight at index 0, secondaryLight at index 1.
- **Methods:**
  - `startTrafficLights()`: void - Initializes traffic light states: mainLight set to GREEN, secondaryLight set to RED.
  - `issueFine()`: void - To be developed. Handles detection and generation of fines for vehicles ignoring signal compliance.
  - `run()`: void - Enables parallel execution, allowing multiple controllers to operate concurrently.
  - `simulateFailure()`: void - Forces error state across intersection to test failsafe logic.
---
## *General Knowledge area*
#### Enumerator Service
Encapsulates the name and corresponding telephone number of Argentina’s core emergency services.
- **Values**
  - `Police(911)` - Telephone number for contacting the police department.
  - `Ambulance(107)` - Direct emergency number for requesting medical assistance.
  - `FireFighters(100)` - Emergency number for the fire department.
  - `CivilDefense(105)` - Direct number for civil defense operations and disaster response.
-----
#### Class SecurityNotice
Stores information about emergency alerts triggered by the system or reported by an operator.
- **Fields:**
  - `description`: String - Short summary of the situation that triggered the emergency response.
  - `services`: Set<Service> - List of emergency services that were contacted (e.g., Police, Ambulance).
  - `geolocation`: EventGeolocation - Provides detailed information about the time and location where the emergency occurred.
---
## *Class UrbanMonitoringCenter*
Central hub of the system. Aggregates core functionalities and information for urban enforcement and emergency response. Imports and integrates `MotorVehicleRegistry`.
- **Fields:**
  - `securityNoticesEffectued`: Set<SecurityNotice> - Collects all emergency alerts generated by the system or its operators.
  - `infractionsTypes`: Set<InfractionType> - Contains all registered infraction categories that may be emitted by the system.
  - `devices`: ArrayList<Device> - Lists all deployed devices participating in the monitoring network (e.g., cameras, traffic lights, radars).
---