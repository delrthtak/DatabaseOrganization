create type privilegeEnum as ENUM('Patient', 'Admin', 'Medical Staff', 'Scheduler');
create type category as enum ('Lab', 'XRay', 'MRI', 'Office Visit');
create type jobs as enum ('Medical Staff', 'Admin', 'Scheduler');
CREATE TABLE Users ( 
	userID INT not null unique, pass varchar(20) not null, privilege privilegeEnum, loginTime TIMESTAMP, logoutTime TIMESTAMP, 
	PRIMARY KEY(userID)
); --Rule 1
CREATE TABLE Employee ( 
	firstName VARCHAR(20), lastName VARCHAR(20), empID INT not null, jobType jobs, 
	foreign key (empID) references Users(userID) on delete cascade, PRIMARY KEY(empID)
); --Rule 1, 3
CREATE TABLE Patient ( 
	firstName VARCHAR(20), lastName VARCHAR(20), address VARCHAR(50), patientID INT not null, 
	foreign key (patientID) references Users(userID) on delete cascade, PRIMARY KEY(patientID)
); --Rule 1, 3

CREATE TABLE Staff ( 
	staffID INT not null primary key,/* firstName VARCHAR(20), lastName VARCHAR(20),*/
	foreign key (staffID) references Employee(empID) on delete cascade
) ; --Rule 1, 3, 8
CREATE TABLE Admins ( 
	adminID INT not null primary key,/* firstName VARCHAR(20), lastName VARCHAR(20),*/
	foreign key (adminID) references Employee on delete cascade
) ; --Rule 1, 3, 8
CREATE TABLE Scheduler ( 
	schedulerID INT not null primary key,/* firstName VARCHAR(20), lastName VARCHAR(20),*/
	foreign key (schedulerID) references Employee on delete cascade
) ; --Rule 1, 3, 8

CREATE TABLE Orders ( 
	orderID INT not null, patientID INT references patient, staffID INT references staff, diagnosticID INT, results VARCHAR(50) , 
	PRIMARY KEY(orderID)
); --Rule 1, 3, 4
CREATE TABLE Diagnostic ( 
	diagnosticID INT not null references Orders, price NUMERIC, categoryType category, prescriber int references Staff(staffID),
	Primary KEY(diagnosticID) ,
	check (price >= 0.0)
); --Rule 2, 3, 4
CREATE TABLE Records ( 
	patientID INT references Patient(patientID), entered timestamp, patientInfo VARCHAR(50) , 
	PRIMARY KEY(patientID, entered)
); --Rule 1, 4, 7
CREATE TABLE Calendar ( 
	time NUMERIC, day INT, month INT, year INT, patientID INT references patient(patientID), schedulerID INT references employee(empID), assignedStaffID INT references employee(empID), 
	PRIMARY KEY(time, day, month, year, assignedStaffID)
); --Rule 1, 4
CREATE TABLE Bill ( 
	patientID INT references Patient(patientid), billID INT, visitCharge NUMERIC, diagnosticCharge numeric, totalCost NUMERIC, orderID INT, description VARCHAR(50) ,
	PRIMARY KEY(billID)
); --Rule 1, 4


 

--grant insert on users to Admins;
--grant update on Calendar to Admins;
--grant insert on Calendar to Admins;








--grant insert on calendar to scheduler;