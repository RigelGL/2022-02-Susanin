package ru.otus.spring.dao;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Repository
public class GenreDaoJdbc implements GenreDao {

    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedJdbc;

    public GenreDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
        this.namedJdbc = namedParameterJdbcOperations;
    }

    @Override
    public int count() {
        Integer count = jdbc.queryForObject("select count(id) from genre", Integer.class);
        return count == null ? 0 : count;
    }

    @Override
    public Genre insert(Genre genreTemplate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", genreTemplate.getName());
        namedJdbc.update("insert into genre (name) values (:name)", params, keyHolder);

        return new Genre(keyHolder.getKey().longValue(), genreTemplate.getName());
    }


    @Override
    public Genre getByName(String name) {
        Map<String, Object> params = Collections.singletonMap("name", name);
        return namedJdbc.queryForObject("select id, name from genre where name = :name", params, new GenreMapper());
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select id, name from genre", new GenreMapper());
    }

    @Override
    public List<Genre> getAllForBook(long bookId) {
        return namedJdbc.query(
                "select g.id, g.name from genre g inner join book_to_genre b2g on b2g.genre_id = g.id where b2g.book_id = :bookId",
                Collections.singletonMap("bookId", bookId), new GenreMapper());
    }

    @Override
    public boolean deleteByName(String name) {
        Map<String, Object> params = Collections.singletonMap("name", name);
        return namedJdbc.update("delete from genre where name = :name", params) > 0;
    }

    @Override
    public boolean update(Genre genre) {
        return namedJdbc.update("update genre set name = :name where id = :id", Map.of("name", genre.getName(), "id", genre.getId())) > 0;
    }


    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Genre(id, name);
        }
    }
}
