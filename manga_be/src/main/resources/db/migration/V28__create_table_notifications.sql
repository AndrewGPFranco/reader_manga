create table if not exists notifications (
    id UUID primary key,
    content varchar not null,
    data_in DATE not null,
    data_out DATE,
    origin varchar(5) not null
);