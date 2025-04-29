create table if not exists roles (
  id serial primary key,
  role varchar(20) not null,
  user_id integer not null,
  FOREIGN KEY(user_id) REFERENCES users(id)
);
