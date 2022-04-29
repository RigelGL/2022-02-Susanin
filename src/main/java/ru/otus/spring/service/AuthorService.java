package ru.otus.spring.service;

import ru.otus.spring.domain.Author;

import java.util.List;


public interface AuthorService {

    List<Author> getAll();

    Author getById(long id);

    boolean deleteById(long id);

    Author addAuthor(Author authorTemplate);

    boolean update(Author author);
}
