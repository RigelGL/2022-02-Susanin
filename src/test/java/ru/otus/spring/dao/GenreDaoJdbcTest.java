package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.spring.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;


@DisplayName("Dao для работы с жанрами")
@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {

    public static final int EXPECTED_GENRES_COUNT = 1;

    public static final int EXPECTED_GENRE_ID = 1;
    public static final String EXPECTED_GENRE_NAME = "fantasy";

    public static final int EXPECTED_NEW_GENRE_ID = 2;
    public static final String NEW_GENRE_NAME = "fun";

    public static final int INVALID_GENRE_ID = 5000;
    public static final String INVALID_GENRE_NAME = "INVALID";

    public static final int TARGET_BOOK_ID = 1;


    @Autowired
    private GenreDaoJdbc dao;

    @Test
    @DisplayName("Получение количества жанров")
    void count() {
        var actualGenresCount = dao.count();
        assertThat(actualGenresCount).isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @Test
    @DisplayName("Добавление жанра")
    void insert() {
        var genreTemplate = new Genre(EXPECTED_NEW_GENRE_ID, NEW_GENRE_NAME);
        var insertedGenre = dao.insert(genreTemplate);
        assertThat(insertedGenre).isEqualTo(genreTemplate);
        assertThatCode(() -> dao.insert(genreTemplate)).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    @DisplayName("Получение по имени")
    void getByName() {
        var expectedGenre = new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME);
        var actualGenre = dao.getByName(EXPECTED_GENRE_NAME);
        assertThat(actualGenre).isEqualTo(expectedGenre);
        assertThatCode(() -> dao.getByName(INVALID_GENRE_NAME)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("Получение всех жанров")
    void getAll() {
        var expectedGenre = new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME);

        var actualGenres = dao.getAll();

        assertThat(actualGenres).contains(expectedGenre);
        assertThat(actualGenres).size().isEqualTo(1);
    }

    @Test
    @DisplayName("Полученеи всех жанров для книги")
    void getAllForBook() {
        var expectedGenre = new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME);

        var actualGenres = dao.getAllForBook(TARGET_BOOK_ID);

        assertThat(actualGenres).contains(expectedGenre);
        assertThat(actualGenres).size().isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @Test
    @DisplayName("Удаление по имени")
    void deleteByName() {
        assertThatCode(() -> dao.getByName(EXPECTED_GENRE_NAME)).doesNotThrowAnyException();
        var success = dao.deleteByName(EXPECTED_GENRE_NAME);
        assertThat(success).isEqualTo(true);
        assertThatCode(() -> dao.getByName(EXPECTED_GENRE_NAME)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("Переименовывание")
    void update() {
        var updatedGenre = new Genre(EXPECTED_GENRE_ID, NEW_GENRE_NAME);
        boolean success = dao.update(updatedGenre);
        assertThat(success).isEqualTo(true);

        assertThat(dao.getByName(updatedGenre.getName())).isEqualTo(updatedGenre);

        success = dao.update(new Genre(INVALID_GENRE_ID, NEW_GENRE_NAME));
        assertThat(success).isEqualTo(false);
    }
}