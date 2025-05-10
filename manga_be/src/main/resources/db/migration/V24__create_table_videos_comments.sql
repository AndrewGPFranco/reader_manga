create table if not exists videos_comments (
    id serial primary key,
    user_id int not null,
    episode_id int not null,
    comment varchar(2000) not null,
    foreign key (user_id) references users(id),
    foreign key (episode_id) references episode(id),
    unique(user_id, episode_id)
);