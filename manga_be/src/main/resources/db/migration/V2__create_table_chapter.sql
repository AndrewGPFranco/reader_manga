create table if not exists chapter (
    id SERIAL PRIMARY KEY,
    title VARCHAR(30) NOT NULL UNIQUE,
    number_pages INT NOT NULL,
    manga_id INT NOT NULL,
    CONSTRAINT fk_manga FOREIGN KEY (manga_id) REFERENCES manga(id)
);