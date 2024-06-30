create table manga (
    id SERIAL PRIMARY KEY,
    title VARCHAR(30) NOT NULL,
    description VARCHAR(255) NOT NULL,
    size_manga INT NOT NULL,
    creation_date VARCHAR(20),
    closing_date VARCHAR(20),
    status VARCHAR(11) NOT NULL,
    author VARCHAR(25) NOT NULL,
    gender VARCHAR(15) NOT NULL,
    image VARCHAR(100) NOT NULL
);