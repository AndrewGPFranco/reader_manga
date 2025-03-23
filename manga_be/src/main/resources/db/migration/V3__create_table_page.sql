create table if not exists page (
    id SERIAL PRIMARY KEY,
    path_page VARCHAR(255) NOT null UNIQUE,
    chapter_id INT NOT NULL,
    CONSTRAINT fk_chapter FOREIGN KEY (chapter_id) REFERENCES chapter(id)
);