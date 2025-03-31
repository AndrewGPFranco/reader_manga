alter table user_chapter
add column status varchar(11) not null default 'ONGOING';

update user_chapter set status = 'ONGOING' where status = null;