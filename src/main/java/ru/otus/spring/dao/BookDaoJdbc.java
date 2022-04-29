package ru.otus.spring.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Repository
public class BookDaoJdbc implements BookDao {

    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedJdbc;
    private final GenreDao genreDao;
    private final AuthorDao authorDao;

    public BookDaoJdbc(
            NamedParameterJdbcOperations namedParameterJdbcOperations,
            GenreDao genreDao,
            AuthorDao authorDao) {
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
        this.namedJdbc = namedParameterJdbcOperations;
        this.genreDao = genreDao;
        this.authorDao = authorDao;
    }


    @Override
    public int count() {
        Integer count = jdbc.queryForObject("select count(id) from book", Integer.class);
        return count == null ? 0 : count;
    }

    @Override
    public Book insert(Book bookTemplate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into book (name, content, author_id) values (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, bookTemplate.getName());
            ps.setString(2, bookTemplate.getContent());
            ps.setLong(3, bookTemplate.getAuthor().getId());
            return ps;
        }, keyHolder);

        Long key = keyHolder.getKeyAs(Long.class);

        if(key == null)
            return null;

        if(bookTemplate.getGenres().size() > 0) {
            // нужно ли так приводить тип?
            var params = (Map<String, Object>[]) new Map[bookTemplate.getGenres().size()];

            for(int i = 0; i < bookTemplate.getGenres().size(); i++) {
                params[i] = Map.of("book", key, "genre", bookTemplate.getGenres().get(i).getId());
            }

            namedJdbc.batchUpdate("insert into book_to_genre (book_id, genre_id) values (:book, :genre)", params);
        }

        return new Book(key,
                bookTemplate.getName(),
                bookTemplate.getContent(),
                bookTemplate.getAuthor(),
                bookTemplate.getGenres()
        );
    }

    @Override
    public Book getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        BookTemplate template = namedJdbc.queryForObject("select id, name, content, author_id from book where id = :id", params, new BookMapper());

        Author author = null;
        try {
            author = authorDao.getById(template.getAuthorId());
        } catch(Exception ignored) {
        }

        return new Book(
                template.getId(),
                template.getName(),
                template.getContent(),
                author,
                genreDao.getAllForBook(template.getId())
        );
    }

    @Override
    public List<Book> getAll() {
        List<BookTemplate> templates = jdbc.query("select id, name, content, author_id from book", new BookMapper());

        List<Book> books = new ArrayList<>(templates.size());

        for(BookTemplate template : templates) {
            Author author = null;
            try {
                author = authorDao.getById(template.getAuthorId());
            } catch(Exception ignored) {
            }

            books.add(new Book(
                    template.getId(),
                    template.getName(),
                    template.getContent(),
                    author,
                    genreDao.getAllForBook(template.getId()))
            );
        }

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

        namedJdbc.update("delete from book_to_genre where book_id = :id", Map.of("id", book.getId()));

        if(book.getGenres().size() > 0) {
            // нужно ли так приводить тип?
            var batchParams = (Map<String, Object>[]) new Map[book.getGenres().size()];

            for(int i = 0; i < book.getGenres().size(); i++) {
                batchParams[i] = Map.of("book", book.getId(), "genre", book.getGenres().get(i).getId());
            }

            namedJdbc.batchUpdate("insert into book_to_genre (book_id, genre_id) values (:book, :genre)", batchParams);
        }

        return affected > 0;
    }


    @Data
    @AllArgsConstructor
    private static class BookTemplate {

        private final long id;
        private final String name;
        private final String content;

        private final long authorId;
    }

    private static class BookMapper implements RowMapper<BookTemplate> {

        @Override
        public BookTemplate mapRow(ResultSet resultSet, int i) throws SQLException {
            return new BookTemplate(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("content"),
                    resultSet.getLong("author_id")
            );
        }
    }

}
