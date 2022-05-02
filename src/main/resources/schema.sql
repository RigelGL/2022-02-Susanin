drop table if exists genre;
create table genre(
    id BIGINT primary key not null auto_increment,
    name varchar(255) not null UNIQUE
);

drop table if exists author;
create table author(
    id BIGINT primary key not null auto_increment,
    name varchar(255) not null
);

drop table if exists book;
create table book (
    id bigint primary key not null auto_increment,
    name varchar(255) not null,
    content longtext not null default '',
    author_id bigint null,

    constraint book_author_id foreign key (author_id) references author (id) on delete set null
);

drop table if exists book_to_genre;
create table book_to_genre (
    book_id bigint not null,
    genre_id bigint not null,

    constraint b2g_book_id foreign key (book_id) references book (id) on delete cascade,
    constraint b2g_genre_id foreign key (genre_id) references genre (id) on delete cascade,
    constraint b2g_uk unique (book_id, genre_id)
);