create table if not exists comment (
    id SERIAL PRIMARY KEY,
    name_user VARCHAR(20) NOT NULL,
    comment_text TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    feedback VARCHAR(15) NOT NULL,
    manga_id INT NOT NULL,
    CONSTRAINT fk_manga_comment FOREIGN KEY (manga_id) REFERENCES manga(id)
);