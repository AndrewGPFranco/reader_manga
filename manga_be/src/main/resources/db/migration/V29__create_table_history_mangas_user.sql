create table if not exists history_mangas_user (
    id UUID PRIMARY KEY,
    user_id SERIAL NOT NULL,
    manga_id SERIAL NOT NULL,
    chapter_id SERIAL NOT NULL,
    status VARCHAR(10) NOT NULL,
    last_check TIMESTAMPTZ NOT NULL
);