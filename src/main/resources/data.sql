-- Users and user roles
INSERT INTO users (username, password) VALUES
('test', '$2a$12$BxH.QaTRK7p4f3r.Gc//YOozw1Po0U6TAVXkl33tSzfEGHizIurPO'),  -- test
('admin', '$2a$12$GP/ecvzGkUoXyQv6nFKaH.uUHApsyNFR7khk9CW6D701VXt2zsRNW'); -- admin

INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO user_roles (user_id, role_id) VALUES (1,1), (2,1), (2,2);

-- Patients
INSERT INTO patients (name, personal_number) VALUES
('Jack Sparrow', '1029472846'),
('Lisa Bond', '118726348612'),
('Carmen SanDiego', '28173681-1928'),
('Leonid Brezhnev', '129786-1828');

INSERT INTO journals (record, patient_id) VALUES
('Sick', 1),
('Very sick', 1),
('Defunct', 2);