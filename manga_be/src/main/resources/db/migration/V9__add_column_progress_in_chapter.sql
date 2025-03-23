alter table chapter
add column reading_progress int not null default(0);

update chapter set reading_progress = 0 where reading_progress = null;