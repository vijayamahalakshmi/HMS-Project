/* ROLES (explicit IDs for seed) */
INSERT INTO ROLES (ID, NAME) VALUES
  (1,'ADMIN'),(2,'DOCTOR'),(3,'PATIENT');

/* USERS (seed explicit IDs 1..13) 
   NOTE: Keep {raw} only if you have startup code that converts to bcrypt.
         Otherwise replace with real {bcrypt} hashes. */
INSERT INTO USERS (ID, USERNAME, EMAIL, PASSWORD_HASH, ROLE_ID) VALUES
  (1,  'admin',    'admin@hms.local',       '{raw}Admin@123',  1),
  (2,  'dr.alice', 'alice.smith@hms.local', '{raw}Doctor@123', 2),
  (3,  'john',     'john@example.com',      '{raw}Patient@123',3),
  (4,  'dr.raj',   'raj.patel@hms.local',   '{raw}Doctor@123', 2),
  (5,  'mary',     'mary@example.com',      '{raw}Patient@123',3),
  (6,  'dr.sara',  'sara.lee@hms.local',    '{raw}Doctor@123', 2),
  (7,  'anil',     'anil@example.com',      '{raw}Patient@123',3),
  (8,  'dr.kumar', 'r.kumar@hms.local',     '{raw}Doctor@123', 2),
  (9,  'priya',    'priya@example.com',     '{raw}Patient@123',3),
  (10, 'dr.jones', 'tom.jones@hms.local',   '{raw}Doctor@123', 2),
  (11, 'arun',     'arun@example.com',      '{raw}Patient@123',3),
  (12, 'dr.meera', 'meera.nair@hms.local',  '{raw}Doctor@123', 2),
  (13, 'kavya',    'kavya@example.com',     '{raw}Patient@123',3);

/* DEPARTMENTS */
INSERT INTO DEPARTMENT (ID, NAME) VALUES
  (1,'Cardiology'),
  (2,'Orthopedics'),
  (3,'Pediatrics'),
  (4,'Neurology'),
  (5,'Dermatology');

/* DOCTORS (link to departments 1..5; USER_ID linked to seeded USERS) */
INSERT INTO DOCTOR (ID, USER_ID, DEPARTMENT_ID, NAME, PHONE, YEARS_EXPERIENCE) VALUES
  (100, 2, 1, 'Dr. Alice Smith', '9991112222',  8),
  (101, 4, 2, 'Dr. Raj Patel',   '9993334444', 12),
  (102, 6, 3, 'Dr. Sara Lee',    '9995556666',  6),
  (103, 8, 4, 'Dr. R. Kumar',    '9997778888', 15),
  (104,10, 1, 'Dr. Tom Jones',   '9992223333',  9),
  (105,12, 5, 'Dr. Meera Nair',  '9994445555', 11);

/* PATIENTS */
INSERT INTO PATIENT (ID, USER_ID, FULL_NAME, EMAIL, PHONE, DOB, ADDRESS) VALUES
  (200, 3,  'John Doe',   'john@example.com',  '8887776666', DATE '1998-05-21', '12 MG Road, Bengaluru, KA'),
  (201, 5,  'Mary Lee',   'mary@example.com',  '7776665555', DATE '2000-01-15', '45 Park Street, Kolkata, WB'),
  (202, 7,  'Anil Verma', 'anil@example.com',  '9876500001', DATE '1997-07-10', '8-2/33 Jubilee Hills, Hyderabad, TS'),
  (203, 9,  'Priya Singh','priya@example.com', '9876500002', DATE '1999-03-03', 'Sector 62, Noida, UP'),
  (204, 11, 'Arun Sharma','arun@example.com',  '9876500003', DATE '1995-12-12', 'Anna Nagar, Chennai, TN'),
  (205, 13, 'Kavya Rao',  'kavya@example.com', '9876500004', DATE '2001-08-22', 'Banjara Hills, Hyderabad, TS');

/* APPOINTMENTS */
INSERT INTO APPOINTMENT (ID, DOCTOR_ID, PATIENT_ID, START_TIME, END_TIME, STATUS, CREATED_AT) VALUES
  (400,100,200,TIMESTAMP '2025-09-12 10:00:00', TIMESTAMP '2025-09-12 10:20:00','BOOKED',    TIMESTAMP '2025-09-05 09:00:00'),
  (401,100,201,TIMESTAMP '2025-09-12 10:30:00', TIMESTAMP '2025-09-12 10:50:00','CONFIRMED', TIMESTAMP '2025-09-05 09:10:00'),
  (402,100,203,TIMESTAMP '2025-09-13 09:00:00', TIMESTAMP '2025-09-13 09:15:00','PENDING',   TIMESTAMP '2025-09-07 08:00:00'),
  (403,101,200,TIMESTAMP '2025-09-13 09:30:00', TIMESTAMP '2025-09-13 09:50:00','BOOKED',    TIMESTAMP '2025-09-06 10:00:00'),
  (404,101,205,TIMESTAMP '2025-09-12 11:00:00', TIMESTAMP '2025-09-12 11:20:00','CONFIRMED', TIMESTAMP '2025-09-06 10:30:00'),
  (405,102,202,TIMESTAMP '2025-09-12 14:00:00', TIMESTAMP '2025-09-12 14:20:00','PENDING',   TIMESTAMP '2025-09-06 12:00:00'),
  (406,102,204,TIMESTAMP '2025-09-12 14:30:00', TIMESTAMP '2025-09-12 14:45:00','CONFIRMED', TIMESTAMP '2025-09-06 12:10:00'),
  (407,103,201,TIMESTAMP '2025-09-13 16:00:00', TIMESTAMP '2025-09-13 16:20:00','BOOKED',    TIMESTAMP '2025-09-07 09:00:00'),
  (408,103,204,TIMESTAMP '2025-09-14 10:00:00', TIMESTAMP '2025-09-14 10:15:00','CONFIRMED', TIMESTAMP '2025-09-07 09:30:00'),
  (409,104,205,TIMESTAMP '2025-09-12 09:00:00', TIMESTAMP '2025-09-12 09:20:00','CANCELLED', TIMESTAMP '2025-09-05 08:00:00'),
  (410,104,203,TIMESTAMP '2025-09-13 12:00:00', TIMESTAMP '2025-09-13 12:15:00','NO_SHOW',   TIMESTAMP '2025-09-07 10:00:00'),
  (411,105,202,TIMESTAMP '2025-09-13 15:00:00', TIMESTAMP '2025-09-13 15:20:00','PENDING',   TIMESTAMP '2025-09-07 11:00:00');

/* PRESCRIPTIONS */
INSERT INTO PRESCRIPTION (ID, APPOINTMENT_ID, NOTES, MEDICINES_TEXT, CREATED_AT) VALUES
  (500,401,'URTI','Paracetamol 500mg: 1 tab BD × 5d; Steam inhalation',CURRENT_TIMESTAMP),
  (501,404,'Sprain','Ibuprofen 400mg: 1 tab TID × 3d; Cold compress',CURRENT_TIMESTAMP),
  (502,406,'Fever','Paracetamol 650mg: 1 tab TID × 3d; ORS as needed',CURRENT_TIMESTAMP),
  (503,408,'Migraine','Sumatriptan 50mg: 1 tab PRN; Sleep hygiene advice',CURRENT_TIMESTAMP),
  (504,410,'Chest pain','Aspirin 75mg: 1 tab OD; Atorvastatin 10mg: 1 tab HS; Follow-up in 1 week',CURRENT_TIMESTAMP);
