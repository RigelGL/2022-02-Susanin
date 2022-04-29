package ru.otus.spring.dao;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedJdbc;

    public AuthorDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations.getJdbcOperations();
        this.namedJdbc = namedParameterJdbcOperations;
    }

    @Override
    public int count() {
        Integer count = jdbc.queryForObject("select count(id) from author", Integer.class);
        return count == null ? 0 : count;
    }

    @Override
    public Author insert(Author authorTemplate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into author (name) values (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, authorTemplate.getName());
            return ps;
        }, keyHolder);

        Long key = keyHolder.getKeyAs(Long.class);

        return key == null ? null : new Author(key, authorTemplate.getName());
    }

    @Override
    public Author getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedJdbc.queryForObject("select id, name from author where id = :id", params, new AuthorDaoJdbc.AuthorMapper());
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select id, name from author", new AuthorDaoJdbc.AuthorMapper());
    }

    @Override
    public boolean deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedJdbc.update("delete from author where id = :id", params) > 0;
    }

    @Override
    public boolean update(Author author) {
        return namedJdbc.update("update author set name = :name where id = :id", Map.of("name", author.getName(), "id", author.getId())) > 0;
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Author(id, name);
        }
    }
}
