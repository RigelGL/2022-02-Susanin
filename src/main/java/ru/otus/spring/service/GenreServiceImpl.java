package ru.otus.spring.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;

import java.util.List;


@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao dao;

    @Override
    public List<Genre> getAll() {
        return dao.getAll();
    }

    @Override
    public Genre getByName(String name) {
        try {
            return dao.getByName(name);
        } catch(DataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean deleteByName(String name) {
        return dao.deleteByName(name);
    }

    @Override
    public Genre addGenre(Genre genreTemplate) {
        try {
            return dao.insert(genreTemplate);
        } catch(DuplicateKeyException e) {
            return null;
        }
    }

    @Override
    public boolean update(Genre genre) {
        try {
            return dao.update(genre);
        } catch(DuplicateKeyException e) {
            return false;
        }
    }
}
