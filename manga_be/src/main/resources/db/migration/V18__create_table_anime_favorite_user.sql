create table if not exists anime_favorite_user (
	id serial primary key,
	user_id integer not null,
	anime_id integer not null,
	foreign key (user_id) references users (id),
	foreign key (anime_id) references anime (id),
	unique (user_id, anime_id)
);