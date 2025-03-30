create table user_chapter (
    id serial primary key,
    user_id int not null,
    chapter_id int not null,
    progress int not null default(0),
    foreign key (user_id) references users (id),
    foreign key (chapter_id) references chapter (id)
);