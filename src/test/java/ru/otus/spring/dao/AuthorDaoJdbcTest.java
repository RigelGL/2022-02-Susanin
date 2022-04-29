package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.spring.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;


@DisplayName("Dao для работы с авторами")
@JdbcTest
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {

    public static final int EXPECTED_AUTHORS_COUNT = 2;

    public static final int EXPECTED_AUTHOR_ID = 1;
    public static final String EXPECTED_AUTHOR_NAME = "Tom";

    public static final int EXPECTED_NEW_AUTHOR_ID = 3;
    public static final String NEW_AUTHOR_NAME = "Oleg";

    public static final int INVALID_AUTHOR_ID = 5000;
    public static final String INVALID_AUTHOR_NAME = "INVALID";


    @Autowired
    private AuthorDaoJdbc dao;

    @Test
    @DisplayName("Получене количества авторов")
    void count() {
        var actualAuthorsCount = dao.count();
        assertThat(actualAuthorsCount).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @Test
    @DisplayName("Добавление автора")
    void insert() {
        var authorTemplate = new Author(EXPECTED_NEW_AUTHOR_ID, NEW_AUTHOR_NAME);
        var insertedAuthor = dao.insert(authorTemplate);
        assertThat(insertedAuthor).isEqualTo(authorTemplate);
    }

    @Test
    @DisplayName("Получение по id")
    void getById() {
        var expectedAuthor = new Author(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_NAME);
        var actualAuthor = dao.getById(EXPECTED_AUTHOR_ID);
        assertThat(expectedAuthor).isEqualTo(actualAuthor);
    }

    @Test
    @DisplayName("Удаление автора")
    void deleteById() {
        assertThatCode(() -> dao.getById(EXPECTED_AUTHOR_ID)).doesNotThrowAnyException();
        var success = dao.deleteById(EXPECTED_AUTHOR_ID);
        assertThat(success).isEqualTo(true);
        assertThatCode(() -> dao.getById(EXPECTED_AUTHOR_ID)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("Получение списка авторов")
    void getAll() {
        var expectedAuthor = new Author(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_NAME);

        var actualAuthors = dao.getAll();

        assertThat(actualAuthors).contains(expectedAuthor);
        assertThat(actualAuthors).size().isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @Test
    @DisplayName("Обновление автора")
    void update() {
        boolean success = dao.update(new Author(INVALID_AUTHOR_ID, INVALID_AUTHOR_NAME));
        assertThat(success).isEqualTo(false);

        success = dao.update(new Author(EXPECTED_AUTHOR_ID, NEW_AUTHOR_NAME));
        assertThat(success).isEqualTo(true);
    }


}