create table if not exists user_manga (
    id SERIAL PRIMARY KEY,
    manga_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    data_assinatura DATE NOT NULL,
    status VARCHAR(25) NOT NULL,
    nota INTEGER,
    comentario TEXT,
    FOREIGN KEY (manga_id) REFERENCES manga (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    UNIQUE (manga_id, user_id)
);