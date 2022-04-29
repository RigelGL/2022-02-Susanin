package ru.otus.spring.service;


import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.Book;

import java.util.List;


@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookDao dao;


    @Override
    public List<Book> getAll() {
        return dao.getAll();
    }

    @Override
    public Book getById(long id) {
        try {
            return dao.getById(id);
        } catch(DataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean deleteById(long id) {
        return dao.deleteById(id);
    }

    @Override
    public Book addBook(Book bookTemplate) {
        return dao.insert(bookTemplate);
    }

    @Override
    public boolean update(Book book) {
        return dao.update(book);
    }
}
