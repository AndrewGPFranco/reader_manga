create table if not exists episode (
    id serial primary key,
    title varchar(255) not null unique,
    uri varchar(255) not null unique,
    anime_id int not null,
    constraint fk_anime foreign key (anime_id) references anime(id)
)