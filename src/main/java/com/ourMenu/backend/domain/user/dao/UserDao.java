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

    public long createUser(SignUpRequest userInfo) {
        String sql = "INSERT INTO user (nickname, email, password) " +
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
        String sql = "SELECT * FROM user WHERE user_id = :userId";
        SqlParameterSource param = new MapSqlParameterSource("userId", userId);
        return jdbcTemplate.queryForMap(sql, param);
    }

    public Map<String, Object> getUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = :email";
        SqlParameterSource param = new MapSqlParameterSource("email", email);
        return jdbcTemplate.queryForMap(sql, param);
    }

    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = :email";
        SqlParameterSource param = new MapSqlParameterSource("email", email);
        int count = jdbcTemplate.queryForObject(sql, param, Integer.class);
        return count > 0;
    }

    public int updatePassword(Long userId, String newPassword) {
        String sql = "UPDATE user SET password = :newPassword WHERE user_id = :userId";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("newPassword", newPassword);
        return jdbcTemplate.update(sql, param);
    }

    public int updateNickname(Long userId, String newNickname) {
        String sql = "UPDATE user SET nickname = :newNickname WHERE user_id = :userId";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("newNickname", newNickname);
        return jdbcTemplate.update(sql, param);
    }

    public int updateProfileImg(Long userId, String imgUrl) {
        String sql = "UPDATE user SET img_url = :profileImg WHERE user_id = :userId";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("profileImg", imgUrl);
        return jdbcTemplate.update(sql, param);
    }

}
