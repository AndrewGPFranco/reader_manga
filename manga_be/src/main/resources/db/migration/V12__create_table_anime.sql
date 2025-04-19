create table if not exists anime (
    id SERIAL PRIMARY KEY,
    title VARCHAR NOT NULL UNIQUE,
    upload_date DATE NOT NULL
);