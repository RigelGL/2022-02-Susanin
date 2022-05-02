package ru.otus.spring.dao;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Repository
public class BookDaoJdbc implements BookDao {

    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedJdbc;
    private final GenreDao genreDao;

    public BookDaoJdbc(
            NamedParameterJdbcOperations namedParameterJdbcOperations,
            GenreDao genreDao) {
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
        this.namedJdbc = namedParameterJdbcOperations;
        this.genreDao = genreDao;
    }


    @Override
    public int count() {
        Integer count = jdbc.queryForObject("select count(id) from book", Integer.class);
        return count == null ? 0 : count;
    }

    private void deleteAllGenresForBook(Book book) {
        namedJdbc.update("delete from book_to_genre where book_id = :id", Map.of("id", book.getId()));
    }

    private void insertAllGenresForBook(Book book) {
        if(book.getGenres().size() == 0) {
            return;
        }

        var params = (Map<String, Object>[]) new Map[book.getGenres().size()];

        for(int i = 0; i < book.getGenres().size(); i++) {
            params[i] = Map.of("book", book.getId(), "genre", book.getGenres().get(i).getId());
        }

        namedJdbc.batchUpdate("insert into book_to_genre (book_id, genre_id) values (:book, :genre)", params);
    }

    @Override
    public Book insert(Book bookTemplate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", bookTemplate.getName());
        params.addValue("content", bookTemplate.getContent());
        params.addValue("authorId", bookTemplate.getAuthor().getId());

        namedJdbc.update("insert into book (name, content, author_id) values (:name, :content, :authorId)", params, keyHolder);

        long key = keyHolder.getKey().longValue();

        Book result = new Book(key,
                bookTemplate.getName(),
                bookTemplate.getContent(),
                bookTemplate.getAuthor(),
                bookTemplate.getGenres()
        );

        insertAllGenresForBook(result);

        return result;
    }

    @Override
    public Book getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        Book book = namedJdbc.queryForObject(
                "select b.id id, b.name name, b.content content, a.name aname, a.id aid from book b inner join author a on b.author_id = a.id where b.id = :id",
                params, new BookMapper());

        book.getGenres().addAll(genreDao.getAllForBook(book.getId()));

        return book;
    }

    @Override
    public List<Book> getAll() {
        List<Book> books = jdbc.query(
                "select b.id id, b.name name, b.content content, a.name aname, a.id aid from book b inner join author a on b.author_id = a.id",
                new BookMapper());

        List<Long> ids = new ArrayList<>();
        Map<Long, Book> mapToFindBook = new HashMap<>();
        for(Book book : books) {
            ids.add(book.getId());
            mapToFindBook.put(book.getId(), book);
        }

        SqlParameterSource params = new MapSqlParameterSource("ids", ids);

        namedJdbc.query(
                "select b2g.book_id bid, b2g.genre_id gid, g.name gn from book_to_genre b2g inner join genre g on b2g.genre_id = g.id where b2g.book_id",
                rs -> {
                    Genre genre = new Genre(rs.getLong("gid"), rs.getString("gn"));
                    mapToFindBook.get(rs.getLong("bid")).getGenres().add(genre);
                }
        );

        return books;
    }

    @Override
    public boolean deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedJdbc.update("delete from book where id = :id", params) > 0;
    }

    @Override
    public boolean update(Book book) {
        Map<String, Object> params = Map.of(
                "id", book.getId(),
                "name", book.getName(),
                "authorId", book.getAuthor().getId(),
                "content", book.getContent());
        int affected = namedJdbc.update("update book set name = :name, author_id = :authorId, content = :content where id = :id", params);

        deleteAllGenresForBook(book);

        insertAllGenresForBook(book);

        return affected > 0;
    }


    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Book(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("content"),
                    new Author(resultSet.getLong("aid"), resultSet.getString("aname")),
                    new ArrayList<>()
            );
        }
    }
}
