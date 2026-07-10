INSERT INTO authors (name, surname, email)
VALUES
('Andrea', 'Froncillo', 'andrea.froncillo@email.com'),
('Mario', 'Rossi', 'mario.rossi@email.com');

INSERT INTO posts (title, body, author_id)
VALUES
(
    'Introduzione a Spring Data JPA',
    'In questo articolo vedremo come utilizzare Spring Data JPA per semplificare l''accesso ai dati.',
    1
),
(
    'REST API con Spring Boot',
    'Creazione di API REST utilizzando Spring Boot e i Controller.',
    2
);

INSERT INTO comments (username, body, post_id)
VALUES
(
    'Luca',
    'Articolo molto interessante, complimenti!',
    1
),
(
    'Giulia',
    'Grazie, mi è stato davvero utile.',
    2
);