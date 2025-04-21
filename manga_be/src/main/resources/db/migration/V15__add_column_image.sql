alter table anime
add column uri_image varchar(300);

update anime
set uri_image = 'placeholder_' || id
where uri_image is null;

alter table anime
alter column uri_image set not null;

alter table anime
add constraint anime_uri_image_key unique(uri_image);