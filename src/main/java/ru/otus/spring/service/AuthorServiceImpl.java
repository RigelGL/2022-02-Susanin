package ru.otus.spring.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.domain.Author;

import java.util.List;


@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao dao;

    @Override
    public List<Author> getAll() {
        return dao.getAll();
    }

    @Override
    public Author getById(long id) {
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
    public Author addAuthor(Author authorTemplate) {
        return dao.insert(authorTemplate);
    }

    @Override
    public boolean update(Author author) {
        return dao.update(author);
    }
}
