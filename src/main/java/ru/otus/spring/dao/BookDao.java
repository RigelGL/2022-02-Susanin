package ru.otus.spring.dao;

import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;


public interface BookDao {

    int count();

    Book insert(Book bookTemplate);

    Book getById(long id);

    List<Book> getAll();

    boolean deleteById(long id);

    boolean update(Book book);
}
