package ru.otus.spring.service;


import ru.otus.spring.domain.Book;

import java.util.List;


public interface BookService {

    List<Book> getAll();

    Book getById(long id);

    boolean deleteById(long id);

    Book addBook(Book bookTemplate);

    boolean update(Book book);
}
