package ru.otus.spring.service;

import ru.otus.spring.domain.Genre;

import java.util.List;


public interface GenreService {

    List<Genre> getAll();

    Genre getByName(String name);

    boolean deleteByName(String name);

    Genre addGenre(Genre genreTemplate);

    boolean update(Genre genreTemplate);
}
