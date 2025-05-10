alter table episode
add column views int;

update episode
set views = 0
where views is null;

alter table episode
alter column views set not null;

alter table episode
add column uploaded DATE;

update episode
set uploaded = '2010-02-01'
where uploaded is null;

alter table episode
alter column uploaded set not null;