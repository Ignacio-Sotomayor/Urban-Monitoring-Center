# 🏙️ Urban Monitoring Center – System Architecture

## 1. Introduction
The Urban Monitoring Center is a Java-based system that simulates city-wide traffic and security monitoring through a distributed network of devices and centralized management.

## 2. System Overview

### Core Functionality
- Traffic monitoring and violation detection
- Security camera surveillance
- Device status management
- Fine processing and documentation
- Geographic visualization of city devices

## 3. Component Architecture

### 🎮 Controller Layer
```markdown
UrbanMonitoringCenter:
- Central system controller (Singleton)
- Manages device network
- Coordinates system responses
- Processes security notices

RegistryNotifier:
- Handles system-wide notifications
- Bridges devices and registry
- Manages fine processing
```

### 💾 Data Access Layer
```markdown
DBConnection:
- PostgreSQL connection management
- Connection pooling
- Query execution

Core DAOs:
- AutomobileDAO: Vehicle registration
- BrandsDAO: Manufacturer data
- ModelsDAO: Vehicle models
- OwnersDAO: Owner records
- FineDAO: Violation management
- SecurityNoticeDAO: Incident tracking
- ServiceDAO: Maintenance records
```

### 📦 Model Layer
```markdown
Automobile Package:
- Vehicle registration (Automobile)
- Brand management
- Model tracking
- Owner information
- Registry operations (MotorVehicleRegistry)

Devices Package:
- Abstract Device base class
- FineIssuerDevice interface
- Specialized devices:
  * TrafficLight
  * Radar
  * SecurityCamera
  * ParkingLotSecurityCamera
- Location tracking
- State management

Fines Package:
- Fine processing
- Violation types
- Photo evidence
- Geolocation tracking
```

### 🖥️ View Layer
```markdown
Main Windows:
- StartWindow: Entry point
- MenuWindow: Navigation
- MapWindow: Geographic interface
- CamerasWindow: Surveillance

Registry Management:
- InsertWindow
- ViewWindow
- ModelsForm
- ModelsTable

Reporting:
- FinesWindow
- DeviceEventsWindow
- SecurityNoticeWindow
- AutomobileFinesWindow
```

## 4. Resource Organization
```markdown
Static Resources:
- map.html/map.js: Mapping interface
- Icons/: System graphics
- videos/: Surveillance recordings
```

## 5. Key System Interactions

### Device Management Flow
1. Devices report status to UrbanMonitoringCenter
2. Controller processes device state changes
3. View layer updates display
4. Failures trigger maintenance records

### Fine Processing Flow
1. FineIssuerDevices detect violations
2. RegistryNotifier processes notifications
3. FineDAO records violation
4. MotorVehicleRegistry updates records
5. Reports generated through view layer

### Security Incident Flow
1. SecurityCameras detect incidents
2. SecurityNotice created
3. UrbanMonitoringCenter processes notice
4. Notification sent to relevant systems
5. Incident recorded in database

## 6. Exception Handling
```markdown
Custom Exceptions:
- DisconnectedTrafficLightException
- UnrepairableDeviceException

Error Management:
- Device failure tracking
- Service request generation
- Status monitoring
```

## 7. Data Persistence
- PostgreSQL database
- Photo/video storage
- PDF report generation
- Geographic data management

## 8. User Interface
- JavaFX-based interface
- Interactive map visualization
- Real-time device monitoring
- Report generation and viewing
- Security notice management

This architecture provides a robust foundation for urban monitoring with clear separation of concerns and modular design.