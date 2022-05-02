insert into genre (`name`) values ('fantasy');
insert into author (`name`) values ('Tom');
insert into author (`name`) values ('Max');
insert into book (name, author_id) values ('best book', 1);
insert into book_to_genre (book_id, genre_id) values (1, 1);