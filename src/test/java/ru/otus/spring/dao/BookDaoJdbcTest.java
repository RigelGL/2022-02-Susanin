package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;


import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;


@DisplayName("Dao для работы с книгами")
@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class})
class BookDaoJdbcTest {

    public static final int EXPECTED_BOOKS_COUNT = 1;

    public static final int EXPECTED_BOOK_ID = 1;
    public static final String EXPECTED_BOOK_NAME = "best book";
    public static final String EXPECTED_BOOK_CONTENT = "";

    public static final int EXPECTED_NEW_BOOK_ID = 2;
    public static final String NEW_BOOK_NAME = "the book";
    public static final String NEW_BOOK_CONTENT = "some text";

    public static final int EXISTING_AUTHOR_ID = 1;
    public static final String EXISTING_AUTHOR_NAME = "Tom";
    public static final int OTHER_EXISTING_AUTHOR_ID = 2;
    public static final String OTHER_EXISTING_AUTHOR_NAME = "Max";

    public static final int EXISTING_GENRE_ID = 1;
    public static final String EXISTING_GENRE_NAME = "fantasy";

    public static final int INVALID_BOOK_ID = 5000;


    private static final Random random = new Random(34895763);

    @Autowired
    private BookDaoJdbc dao;

    @Autowired
    private GenreDaoJdbc genreDaoJdbc;

    @DisplayName("Получение количества книг")
    @Test
    void count() {
        var actualBooksCount = dao.count();
        assertThat(actualBooksCount).isEqualTo(EXPECTED_BOOKS_COUNT);
    }

    @DisplayName("Добавление книги")
    @Test
    void insert() {
        var existingAuthor = new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME);
        var existingGenre = new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME);
        var bookTemplate = new Book(EXPECTED_NEW_BOOK_ID, NEW_BOOK_NAME, NEW_BOOK_CONTENT, existingAuthor, List.of(existingGenre));
        var insertedBook = dao.insert(bookTemplate);
        assertThat(insertedBook).isEqualTo(bookTemplate);
    }

    @DisplayName("Получение по id")
    @Test
    void getById() {
        var existingAuthor = new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME);
        var existingGenre = new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME);

        var expectedBook = new Book(EXPECTED_BOOK_ID, EXPECTED_BOOK_NAME, EXPECTED_BOOK_CONTENT, existingAuthor, List.of(existingGenre));
        var actualBook = dao.getById(EXPECTED_BOOK_ID);
        assertThat(actualBook).isEqualTo(expectedBook);

        assertThatCode(() -> dao.getById(INVALID_BOOK_ID)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("Получение всех книг")
    @Test
    void getAll() {
        var existingAuthor = new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME);
        var existingGenre = new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME);

        var expectedBook = new Book(EXPECTED_BOOK_ID, EXPECTED_BOOK_NAME, EXPECTED_BOOK_CONTENT, existingAuthor, List.of(existingGenre));
        var actualBooks = dao.getAll();
        assertThat(actualBooks).size().isEqualTo(EXPECTED_BOOKS_COUNT);
        assertThat(actualBooks).contains(expectedBook);
    }

    @DisplayName("Удаление по id")
    @Test
    void deleteById() {
        boolean success = dao.deleteById(INVALID_BOOK_ID);
        assertThat(success).isEqualTo(false);

        success = dao.deleteById(EXPECTED_BOOK_ID);
        assertThat(success).isEqualTo(true);
    }

    @DisplayName("Обновление")
    @Test
    void update() {
        Author otherExpectedAuthor = new Author(OTHER_EXISTING_AUTHOR_ID, OTHER_EXISTING_AUTHOR_NAME);
        boolean success = dao.update( new Book(EXPECTED_BOOK_ID, "new name", "new content", otherExpectedAuthor, new ArrayList<>()));
        assertThat(success).isEqualTo(true);

        success = dao.update(new Book(INVALID_BOOK_ID, "new name", "new content", otherExpectedAuthor, new ArrayList<>()));
        assertThat(success).isEqualTo(false);
    }

    private String generateRandomString(int length) {
        return random.ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


    @DisplayName("Тест на производительность")
//    @Test
    void performance() {
        int booksCount = 1000;
        int genresCount = 1000;
        int genresPerBook = 7;

        List<Genre> genres = new ArrayList<>();
        for(int i = 0; i < genresCount; i++) {
            genres.add(genreDaoJdbc.insert(new Genre(0, generateRandomString(10))));
        }

        dao.deleteById(EXPECTED_BOOK_ID);

        List<Book> books = new ArrayList<>();

        Author author = new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME);

        for(int i = 0; i < booksCount; i++) {
            List<Genre> bookGenres = new ArrayList<>();
            for(int j = 0; j < genresPerBook; j++) {
                Genre genre = genres.get(random.nextInt(genres.size()));
                if(bookGenres.contains(genre))
                    continue;
                bookGenres.add(genre);
            }
            books.add(dao.insert(new Book(0, generateRandomString(10), generateRandomString(100), author, bookGenres)));
        }

        long time = System.nanoTime();
        List<Book> allBooks = dao.getAll();
        time = System.nanoTime() - time;
        System.out.println("TIME: " + (time / 1000) + " us");

        for(Book book : allBooks) {
            book.getGenres().sort(Comparator.comparing(Genre::getId));
        }

        for(Book book : books) {
            book.getGenres().sort(Comparator.comparing(Genre::getId));
        }

        assertThat(allBooks).isEqualTo(books);
    }
}