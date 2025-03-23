alter table chapter
add column status varchar(11) not null default 'ONGOING';

update chapter set status = 'ONGOING' where status = null;