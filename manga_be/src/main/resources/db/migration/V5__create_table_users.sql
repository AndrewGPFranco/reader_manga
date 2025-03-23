create table if not exists users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(255) NOT NULL DEFAULT 'USER',
    date_birth DATE NOT NULL
);