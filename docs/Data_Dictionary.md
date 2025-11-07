# Urban Monitoring Center - Data Dictionary

## Controller Package
#### Class UrbanMonitoringCenter
- Purpose: Main controller managing all system operations
- Key Methods:
  - `getInstance()`: Returns singleton instance
  - `addDevice(Device)`: Registers new monitoring device
  - `removeDevice(Device)`: Removes device from system
  - `notifyFailure(Device, String)`: Reports device malfunction
  - `processSecurityNotice(SecurityNotice)`: Handles security events

#### Class RegistryNotifier
- Purpose: Handles system-wide notifications
- Key Methods:
  - `notifyFine(Fine)`: Processes fine notifications
  - `notifySecurityEvent(SecurityNotice)`: Handles security alerts

## DAO Package
Collection of Data Access Objects for database operations.

#### Class DBConnection
Database connection manager.
- Handles PostgreSQL database connections
- Implements connection pooling
- Key Methods:
  - `getConnection()`: Provides database connection
  
#### Primary DAOs:
## AutomobileDAO:
- `create(Automobile)`: Registers new vehicle
- `findByLicensePlate(String)`: Retrieves vehicle data
- `update(Automobile)`: Updates vehicle information

## BrandsDAO:
- `getAllBrands()`: Lists all manufacturers
- `addBrand(Brand)`: Registers new brand

## FineDAO:
- `createFine(Fine)`: Records new violation
- `getFinesByAutomobile(String)`: Gets vehicle fines

## InfractionTypesDAO:
- `getInfractionTypes()`: Lists violation types
- `addInfractionType(InfractionType)`: Adds new violation type

## ModelsDAO:
- `getModelsByBrand(String)`: Lists brand models
- `addModel(Model)`: Adds new model

## OwnersDAO:
- `createOwner(Owner)`: Registers new owner
- `findByLegalId(String)`: Retrieves owner data

## PhotosDAO:
- `savePhoto(Photo)`: Stores violation evidence
- `getPhotosByFine(Integer)`: Retrieves fine photos

## SecurityNoticeDAO/DetailsDAO:
- `createSecurityNotice(SecurityNotice)`: Records incident
- `getNoticeDetails(Integer)`: Retrieves incident details

## ServiceDAO:
- `createService(Service)`: Records maintenance
- `updateServiceStatus(Integer)`: Updates service status

## Model Package

### Automobile Subpackage
## Automobile:
- Fields:
  - licensePlate: String (unique identifier)
  - brand: Brand
  - model: Model
  - owner: Owner
  - year: Integer
- Methods:
  - getters/setters for all fields
  - equals(): Compares by license plate

## Brand:
- Fields:
  - name: String
  - models: Set<Model>
- Methods:
  - addModel(Model)
  - getModels()

## Model:
- Fields:
  - name: String
  - brand: Brand
- Methods:
  - getName()
  - getBrand()

## MotorVehicleRegistry:
- Fields:
  - automobilesInformation: Map<Automobile, List<Fine>>
  - brands: Set<Brand>
- Methods:
  - addAutomobile(Automobile)
  - addFine(Fine)
  - getScoring(Automobile)

## Owner:
- Fields:
  - fullName: String
  - address: String
  - legalId: String
- Methods:
  - getters/setters for all fields

### Devices Subpackage
## Device (Abstract):
- Fields:
  - id: UUID
  - location: Location
  - state: DeviceState
- Methods:
  - getId()
  - getState()
  - setState(DeviceState)

Specific Devices:
## ParkingLotSecurityCamera:
- Methods:
  - recordVideo()
  - takePicture()

##  Radar:
- Methods:
  - checkSpeed(Automobile)
  - issueFine(Automobile)

## SecurityCamera:
- Methods:
  - startSurveillance()
  - stopSurveillance()

## TrafficLight:
- Fields:
  - currentState: TrafficLightState
- Methods:
  - changeState()
  - getCurrentState()

Supporting Classes:
- DeviceState: OPERATIONAL, FAILED, DISCONNECTED
- Location: latitude/longitude coordinates
- TrafficLightState: RED, YELLOW, GREEN

### Fines Subpackage
##Fine:
- Fields:
  - id: Integer
  - amount: BigDecimal
  - photos: Set<Photo>
  - automobile: Automobile
  - infractionType: InfractionType
- Methods:
  - calculateAmount()
  - addPhoto(Photo)

#### Specialized Classes:
- **ExcessiveSpeedFine**: Speed fine details
- **EventGeolocation**: Location tracking
- **InfractionType**: Violation categories
## EventGeolocation:
- Fields:
  - latitude: Double
  - longitude: Double
  - timestamp: LocalDateTime
## Photo:
- Fields:
  - path: String
  - timestamp: LocalDateTime
#### Support Classes
## ExcessiveSpeed
- Fields:
  - speedLimit: Double
  - actualSpeed: Double
- Methods:
  - calculateExcess()

### Exception Classes
- **DisconnectedTrafficLightException**
- **UnrepairableDeviceException**

### Utility Classes
- **FailureRecord**: Device failure tracking
- **PDFGenerator**: Report generation
- **SecurityNotice**: Security incident records
- **Service**: Maintenance service records

## View Package
User interface components organized by function:

Main Windows:
- StartWindow: Application entry point
- MenuWindow: Main navigation interface
- MapWindow: Geographic visualization
- CamerasWindow: Camera management interface

Registry Management:
- InsertWindow: Data entry forms
- ViewWindow: Data visualization
- ModelsForm: Model management
- ModelsTable: Model listing

Reports:
- AutomobileFinesWindow: Fine history
- DeviceEventsWindow: Device activity log
- FinesWindow: Fine management
- SecurityNoticeWindow: Incident reporting

Supporting Views:
- FinesTable: Fine listings
- SecurityNoticeDialog: Incident input form
- SecurityNoticesTable: Incident overview