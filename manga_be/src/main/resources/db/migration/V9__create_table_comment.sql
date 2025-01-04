CREATE TABLE comment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name_user VARCHAR(20) NOT NULL UNIQUE,
    comment_text TEXT NOT NULL,
    timestamp DATETIME NOT NULL,
    feedback VARCHAR(11) NOT NULL,
    manga_id INT NOT NULL,
    FOREIGN KEY (manga_id) REFERENCES manga(id)
);