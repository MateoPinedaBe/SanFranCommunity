INSERT INTO users (id, names, id_document, email, password, role, sub_role) VALUES
('11111111-1111-1111-1111-111111111111', 'Ana Maria Perez', 'CC12345', 'ana.perez@sanfran.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'RESIDENT', 'OWNER'),
('22222222-2222-2222-2222-222222222222', 'Carlos Gomez', 'CC54321', 'carlos.gomez@sanfran.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STAFF', 'GUEST'),
('33333333-3333-3333-3333-333333333333', 'Laura Rodriguez', 'CC77889', 'laura.rodriguez@sanfran.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 'TENANT');

INSERT INTO users (id, names, id_document, email, password, role, sub_role) VALUES
('44444444-4444-4444-4444-444444444444', 'Admin SanFran', 'CC99999', 'admin@sanfran.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 'TENANT');

INSERT INTO facilities (id, name, description, image_url, capacity) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Salon Social', 'Salon principal para eventos de la comunidad.', 'https://example.com/salon-social.jpg', 120),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Cancha Multiple', 'Espacio deportivo para futbol y baloncesto.', 'https://example.com/cancha-multiple.jpg', 40),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Piscina', 'Piscina para adultos y ninos con zonas separadas.', 'https://example.com/piscina.jpg', 60);

INSERT INTO reservations (id, user_id, facility_id, date, start_time, end_time, status) VALUES
(RANDOM_UUID(), '11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', DATEADD('DAY', 1, CURRENT_DATE), TIME '09:00:00', TIME '11:00:00', 'PENDING'),
(RANDOM_UUID(), '22222222-2222-2222-2222-222222222222', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', DATEADD('DAY', 2, CURRENT_DATE), TIME '14:00:00', TIME '16:00:00', 'PENDING'),
(RANDOM_UUID(), '33333333-3333-3333-3333-333333333333', 'cccccccc-cccc-cccc-cccc-cccccccccccc', DATEADD('DAY', 3, CURRENT_DATE), TIME '18:00:00', TIME '20:00:00', 'PENDING');

