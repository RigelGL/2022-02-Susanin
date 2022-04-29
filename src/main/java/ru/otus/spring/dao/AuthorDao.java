package ru.otus.spring.dao;

import ru.otus.spring.domain.Author;

import java.util.List;


public interface AuthorDao {

    int count();

    Author insert(Author authorTemplate);

    Author getById(long id);

    List<Author> getAll();

    boolean deleteById(long id);

    boolean update(Author author);
}
