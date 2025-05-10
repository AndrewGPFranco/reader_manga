create table if not exists favorite_episode (
    id serial primary key,
    user_id integer not null,
    episode_id integer not null,
    feedback varchar(13) not null,
    foreign key (user_id) references users(id),
    foreign key (episode_id) references episode(id),
    unique (user_id, episode_id)
);