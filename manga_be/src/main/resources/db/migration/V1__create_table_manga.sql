create table if not exists manga (
    id SERIAL PRIMARY KEY,
    title VARCHAR(30) NOT null UNIQUE,
    description TEXT NOT NULL,
    size_manga INT NOT NULL,
    creation_date DATE NOT NULL,
    closing_date DATE,
    status VARCHAR(11) NOT NULL,
    author VARCHAR(25) NOT NULL,
    gender VARCHAR(15) NOT NULL,
    image VARCHAR(350) NOT null,
    favorite BOOLEAN default FALSE
);