alter table anime
add column release_date date;

update anime
set release_date = '2010-02-01'
where release_date is null;

alter table anime
alter column release_date set not null;