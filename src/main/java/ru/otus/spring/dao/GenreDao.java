package ru.otus.spring.dao;

import ru.otus.spring.domain.Genre;


import java.util.List;

public interface GenreDao {

    int count();

    Genre insert(Genre genreTemplate);

    Genre getByName(String name);

    List<Genre> getAll();

    List<Genre> getAllForBook(long bookId);

    boolean deleteByName(String name);

    boolean update(Genre genre);
}
