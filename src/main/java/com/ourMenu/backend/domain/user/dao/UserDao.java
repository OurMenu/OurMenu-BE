package com.ourMenu.backend.domain.user.dao;

import com.ourMenu.backend.domain.user.api.request.SignUpRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

@Repository
public class UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @PostConstruct
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "nickname VARCHAR(255) NOT NULL, " +
                "email VARCHAR(255) NOT NULL UNIQUE, " +
                "password VARCHAR(255) NOT NULL, " +
                "img_url VARCHAR(255), " +
                "status VARCHAR(50) DEFAULT 'CREATED', " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ")";
        jdbcTemplate.getJdbcOperations().execute(sql);
    }

    public long createUser(SignUpRequest userInfo) {
        String sql = "INSERT INTO users (nickname, email, password) " +
                "VALUES (:nickname, :email, :password)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nickname", userInfo.getNickname())
                .addValue("email", userInfo.getEmail())
                .addValue("password", userInfo.getPassword());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Map<String, Object> getUserById(Long userId) {
        String sql = "SELECT * FROM users WHERE user_id = :userId";
        SqlParameterSource param = new MapSqlParameterSource("userId", userId);
        return jdbcTemplate.queryForMap(sql, param);
    }

    public Map<String, Object> getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = :email";
        SqlParameterSource param = new MapSqlParameterSource("email", email);
        return jdbcTemplate.queryForMap(sql, param);
    }

    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = :email";
        SqlParameterSource param = new MapSqlParameterSource("email", email);
        int count = jdbcTemplate.queryForObject(sql, param, Integer.class);
        return count > 0;
    }

}
