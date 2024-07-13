create table chapter (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(30) NOT NULL,
    description VARCHAR(255) NOT NULL,
    number_pages INT NOT NULL,
    manga_id INT NOT NULL,
    FOREIGN KEY (manga_id) REFERENCES manga(id)
);