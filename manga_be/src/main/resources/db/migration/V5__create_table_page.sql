create table page (
    id INT PRIMARY KEY AUTO_INCREMENT,
    page VARCHAR(255) NOT NULL,
    chapter_id INT NOT NULL,
    FOREIGN KEY (chapter_id) REFERENCES chapter(id)
);