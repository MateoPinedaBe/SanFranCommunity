INSERT INTO users (id, names, id_document, email, password, role, sub_role) VALUES
(RANDOM_UUID(), 'Ana Maria Perez', 'CC12345', 'ana.perez@sanfran.com', 'Password123', 'RESIDENT', 'OWNER'),
(RANDOM_UUID(), 'Carlos Gomez', 'CC54321', 'carlos.gomez@sanfran.com', 'Password123', 'STAFF', 'GUEST'),
(RANDOM_UUID(), 'Laura Rodriguez', 'CC77889', 'laura.rodriguez@sanfran.com', 'Password123', 'ADMIN', 'TENANT');

INSERT INTO facilities (id, name, description, image_url, capacity) VALUES
(RANDOM_UUID(), 'Salon Social', 'Salon principal para eventos de la comunidad.', 'https://example.com/salon-social.jpg', 120),
(RANDOM_UUID(), 'Cancha Multiple', 'Espacio deportivo para futbol y baloncesto.', 'https://example.com/cancha-multiple.jpg', 40),
(RANDOM_UUID(), 'Piscina', 'Piscina para adultos y ninos con zonas separadas.', 'https://example.com/piscina.jpg', 60);

INSERT INTO reservations (id, date, start_time, end_time) VALUES
(RANDOM_UUID(), DATEADD('DAY', 1, CURRENT_DATE), TIME '09:00:00', TIME '11:00:00'),
(RANDOM_UUID(), DATEADD('DAY', 2, CURRENT_DATE), TIME '14:00:00', TIME '16:00:00'),
(RANDOM_UUID(), DATEADD('DAY', 3, CURRENT_DATE), TIME '18:00:00', TIME '20:00:00');
