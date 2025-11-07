CREATE TABLE Brands( 
    Brand_ID Serial PRIMARY KEY,
    Brand_Name VARCHAR(30) NOT NULL UNIQUE
);
CREATE TABLE Models( 
    Models_ID Serial PRIMARY KEY,
    Model_Name VARCHAR(30) NOT NULL,
    Brand_ID INTEGER REFERENCES Brands(Brand_ID) ON DELETE NO ACTION ON UPDATE CASCADE
);
CREATE TABLE OWNERS(  
    Owner_ID Serial PRIMARY KEY,
    Owner_LegalID VARCHAR(9) UNIQUE,
    Owner_FullName VARCHAR(60) NOT NULL,
    Owner_Address VARCHAR(50)
);
CREATE TABLE Automobiles(
    Automobile_ID Serial PRIMARY KEY,
    license_Plate VARCHAR(10) NOT NULL UNIQUE,
    Automobile_Year INTEGER CHECK (Automobile_Year >= 1886 AND Automobile_Year <= EXTRACT(YEAR FROM CURRENT_DATE)),
    Model_ID INTEGER REFERENCES Models(Models_ID) ON DELETE NO ACTION ON UPDATE CASCADE,
    Owner_ID INTEGER REFERENCES OWNERS(Owner_ID) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE InfractionTypes( 
    InfractionType_ID Serial PRIMARY KEY,
    InfractionType_Name VARCHAR(40) NOT NULL UNIQUE,
    InfractionType_Description VARCHAR(100) NOT NULL UNIQUE,
    InfractionType_Scoring INTEGER CHECK (InfractionType_Scoring > 0),
    InfractionType_Amount DECIMAL(10,2) CHECK (InfractionType_Amount > 0),
    surchangePer10PercentExcess DECIMAL(10,2)
);
CREATE TABLE Fines(
    Fine_ID Serial PRIMARY KEY,
    Fine_Amount DECIMAL(10,2) CHECK (Fine_Amount >= 0) NOT NULL,
    Fine_Scoring INTEGER CHECK (Fine_Scoring >= 0) NOT NULL,
    Fine_Latitude DECIMAL(9,6) CHECK (Fine_Latitude >= -90 AND Fine_Latitude <= 90) NOT NULL,
    Fine_Longitude DECIMAL(9,6) CHECK (Fine_Longitude >= -180 AND Fine_Longitude <= 180) NOT NULL,
    Fine_Address VARCHAR(100) NOT NULL,
    Fine_DateTime TIMESTAMP NOT NULL,
    Issuer_DeviceUUID VARCHAR(36) NOT NULL,
    Automobile_ID INTEGER REFERENCES Automobiles(Automobile_ID) ON DELETE NO ACTION ON UPDATE CASCADE,
    InfractionType_ID INTEGER REFERENCES InfractionTypes(InfractionType_ID) ON DELETE NO ACTION ON UPDATE CASCADE,
    SpeedLimit DOUBLE PRECISION,
    AutomobileSpeed DOUBLE PRECISION
);
CREATE TABLE Photos(
    Photo_ID Serial PRIMARY KEY,
    Photo_Path VARCHAR(100) NOT NULL,
    Fine_ID INTEGER REFERENCES Fines(Fine_ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE SecuityNotices(
    SecurityNotice_ID Serial PRIMARY KEY,
    SecurityNotice_Description VARCHAR(100) NOT NULL,
    SecurityNotice_Latitude DECIMAL(9,6) CHECK (SecurityNotice_Latitude >= -90 AND SecurityNotice_Latitude <= 90) NOT NULL,
    SecurityNotice_Longitude DECIMAL(9,6) CHECK (SecurityNotice_Longitude >= -180 AND SecurityNotice_Longitude <= 180),
    SecurityNotice_Address VARCHAR(100) NOT NULL,
    SecurityNotice_DateTime TIMESTAMP NOT NULL,
    Issuer_DeviceUUID VARCHAR(34) NOT NULL
);
CREATE TABLE Services(
    Service_ID Serial PRIMARY KEY,
    Service_Name VARCHAR(25) NOT NULL,
    Sevice_PhoneNumber VARCHAR(25) NOT NULL
);
CREATE TABLE SecurityNoticeDetails(
    SecurityNoticeDetail_ID Serial PRIMARY KEY,
    Service_ID INTEGER REFERENCES Services(Service_ID) ON DELETE CASCADE ON UPDATE CASCADE,
    SecurityNotice_ID INTEGER REFERENCES SecuityNotices(SecurityNotice_ID) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Brands (Brand_Name) VALUES ('Toyota');
INSERT INTO Brands (Brand_Name) VALUES ('Ford');
INSERT INTO Brands (Brand_Name) VALUES ('Chevrolet');
INSERT INTO Brands (Brand_Name) VALUES ('Honda');
INSERT INTO Brands (Brand_Name) VALUES ('Nissan');
INSERT INTO Brands (Brand_Name) VALUES ('Volkswagen');

INSERT INTO Models (Model_Name, Brand_ID) VALUES ('Camry', 1);
INSERT INTO Models (Model_Name, Brand_ID) VALUES ('RAV4', 1);
INSERT INTO Models (Model_Name, Brand_ID) VALUES ('Mustang', 2);
INSERT INTO Models (Model_Name, Brand_ID) VALUES ('F-150', 2);
INSERT INTO Models (Model_Name, Brand_ID) VALUES ('Corolla', 1);
INSERT INTO Models (Model_Name, Brand_ID) VALUES ('Explorer', 2);
INSERT INTO Models (Model_Name, Brand_ID) VALUES ('Malibu', 3);
INSERT INTO Models (Model_Name, Brand_ID) VALUES ('Silverado', 3);
Insert INTO Models (Model_Name, Brand_ID) VALUES ('Equinox', 3);
INSERT INTO Models (Model_Name, Brand_ID) VALUES ('Civic', 4);
INSERT INTO Models (Model_Name, Brand_ID) VALUES ('Accord', 4);
INSERT INTO Models (Model_Name, Brand_ID) VALUES ('Altima', 5);
INSERT INTO Models (Model_Name, Brand_ID) VALUES ('Sentra', 5);
INSERT INTO Models (Model_Name, Brand_ID) VALUES ('Golf', 6);
INSERT INTO Models (Model_Name, Brand_ID) VALUES ('Passat', 6);
INSERT INTO Models (Model_Name, Brand_ID) VALUES ('Tiguan', 6);

INSERT INTO OWNERS (Owner_LegalID, Owner_FullName, Owner_Address) VALUES ('12345678', 'John Doe', '123 Main St');
INSERT INTO OWNERS (Owner_LegalID, Owner_FullName, Owner_Address) VALUES ('87654321', 'Jane Smith', '456 Oak Ave');
INSERT INTO OWNERS (Owner_LegalID, Owner_FullName, Owner_Address) VALUES ('11223344', 'Alice Johnson', '789 Pine Rd');
INSERT INTO OWNERS (Owner_LegalID, Owner_FullName, Owner_Address) VALUES ('44332211', 'Bob Brown', '321 Maple Ln');
INSERT INTO OWNERS (Owner_LegalID, Owner_FullName, Owner_Address) VALUES ('55667788', 'Charlie Davis', '654 Cedar St');
INSERT INTO OWNERS (Owner_LegalID, Owner_FullName, Owner_Address) VALUES ('99887766', 'Eve Wilson', '987 Birch Blvd');

INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('AB123CD', 2020, 1, 1);
INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('EF456GH', 2022, 2, 2);
INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('IJ789KL', 2018, 3, 3);
INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('MN012OP', 2021, 4, 4);
INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('QR345ST', 2025, 5, 5);
INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('UV678WX', 2021, 6, 6);
INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('YZ901AB', 2023, 7, 1);
INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('CD234EF', 2022, 8, 2);
INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('GH567IJ', 2019, 9, 3);
INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('KL890MN', 2021, 10, 4);
INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('OP123QR', 2019, 11, 5);
INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('ST456UV', 2024, 12, 6);
INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('WX789YZ', 2025, 13, 1);
INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('AB012CD', 2023, 14, 2);
INSERT INTO Automobiles (license_Plate, Automobile_Year, Model_ID, Owner_ID) VALUES ('EF456GI', 2020, 15, 3);

INSERT INTO InfractionTypes (InfractionType_Name, InfractionType_Description, InfractionType_Scoring, InfractionType_Amount, surchangePer10PercentExcess) VALUES
('Speeding', 'The automobile was captured driving over the speed limit', 2, 217800, 20);
INSERT INTO InfractionTypes (InfractionType_Name, InfractionType_Description, InfractionType_Scoring, InfractionType_Amount) VALUES 
('RedLightViolation', 'The automobile was captured running a red light', 5, 141600);
INSERT INTO InfractionTypes (InfractionType_Name, InfractionType_Description, InfractionType_Scoring, InfractionType_Amount) VALUES 
('ParkingOverTime', 'The automobile was captured parked in a no-parking zone for too much time', 3, 70800);

INSERT INTO Services (Service_name, Sevice_PhoneNumber) VALUES ('Police', '911');
INSERT INTO Services (Service_name, Sevice_PhoneNumber) VALUES ('Fire Department', '100');
INSERT INTO Services (Service_name, Sevice_PhoneNumber) VALUES ('Ambulance', '107');
INSERT INTO Services (Service_name, Sevice_PhoneNumber) VALUES ('Civil Defense', '105');
INSERT INTO Services (Service_name, Sevice_PhoneNumber) VALUES ('Road Assistance', '0223-15-5160671');