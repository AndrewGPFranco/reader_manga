create table if not exists manga_favorite_user (
	id serial primary key,
	user_id integer not null,
	manga_id integer not null,
	foreign key (user_id) references users (id),
	foreign key (manga_id) references manga (id),
	unique (user_id, manga_id)
);