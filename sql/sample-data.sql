USE cms;

SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM doctor_available_times;
DELETE FROM appointment;
DELETE FROM admin;
DELETE FROM patient;
DELETE FROM doctor;
SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE doctor AUTO_INCREMENT = 1;
ALTER TABLE patient AUTO_INCREMENT = 1;
ALTER TABLE appointment AUTO_INCREMENT = 1;
ALTER TABLE admin AUTO_INCREMENT = 1;

INSERT INTO doctor (email, name, password, phone, specialty) VALUES
('dr.adams@example.com', 'Dr. Emily Adams', 'pass12345', '5551012020', 'Cardiologist'),
('dr.johnson@example.com', 'Dr. Mark Johnson', 'secure4567', '5552023030', 'Neurologist'),
('dr.lee@example.com', 'Dr. Sarah Lee', 'leePass987', '5553034040', 'Orthopedist'),
('dr.wilson@example.com', 'Dr. Tom Wilson', 'wilsonPwd', '5554045050', 'Pediatrician'),
('dr.brown@example.com', 'Dr. Alice Brown', 'brownie123', '5555056060', 'Dermatologist');

INSERT INTO doctor_available_times (doctor_id, available_times) VALUES
(1, '09:00-10:00'),
(1, '10:00-11:00'),
(1, '14:00-15:00'),
(2, '09:00-10:00'),
(2, '10:00-11:00'),
(2, '11:00-12:00'),
(3, '11:00-12:00'),
(3, '14:00-15:00'),
(4, '13:00-14:00'),
(5, '15:00-16:00');

INSERT INTO patient (address, email, name, password, phone) VALUES
('101 Oak St, Cityville', 'jane.doe@example.com', 'Jane Doe', 'passJane1', '8881111111'),
('202 Maple Rd, Townsville', 'john.smith@example.com', 'John Smith', 'smithSecure', '8882222222'),
('303 Pine Ave, Villageton', 'emily.rose@example.com', 'Emily Rose', 'emilyPass99', '8883333333'),
('404 Birch Ln, Metropolis', 'michael.j@example.com', 'Michael Jordan', 'airmj23', '8884444444'),
('505 Cedar Blvd, Springfield', 'olivia.m@example.com', 'Olivia Moon', 'moonshine12', '8885555555');

INSERT INTO appointment (appointment_time, status, doctor_id, patient_id) VALUES
('2025-04-15 09:00:00.000000', 1, 1, 1),
('2025-04-15 10:00:00.000000', 1, 1, 2),
('2025-04-15 11:00:00.000000', 1, 1, 3),
('2025-04-15 09:30:00.000000', 1, 2, 4),
('2025-04-15 13:00:00.000000', 1, 3, 5),
('2025-04-01 09:00:00.000000', 1, 1, 4),
('2025-04-02 10:00:00.000000', 1, 1, 5),
('2025-04-03 11:00:00.000000', 1, 2, 1),
('2025-04-04 14:00:00.000000', 1, 2, 2),
('2025-04-05 15:00:00.000000', 1, 3, 3),
('2025-05-01 09:00:00.000000', 0, 4, 1),
('2025-05-02 10:00:00.000000', 0, 5, 2),
('2025-06-10 11:00:00.000000', 0, 1, 3),
('2025-06-11 14:00:00.000000', 0, 2, 4),
('2025-07-12 15:00:00.000000', 0, 3, 5);

INSERT INTO admin (username, password) VALUES
('admin', 'admin@1234');
