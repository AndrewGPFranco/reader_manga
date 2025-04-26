create table anime_user (
    id serial primary key,
    user_id integer not null,
    anime_id integer not null,
    note integer,
    progress integer default 0,
    status varchar(25) not null default 'ONGOING',
    foreign key (user_id) references users(id),
    foreign key (anime_id) references anime(id)
)