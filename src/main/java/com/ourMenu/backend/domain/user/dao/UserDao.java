package com.ourMenu.backend.domain.user.dao;

import com.ourMenu.backend.domain.user.api.request.SignUpRequest;
import com.ourMenu.backend.domain.user.api.response.UserArticleResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
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

    public List<UserArticleResponse> getUserArticles(Long userId, Long startId, int size) {
        String sql = "SELECT " +
                "a.article_id AS id, " +
                "a.title, " +
                "a.content, " +
                "u.nickname AS creator, " +
                "u.img_url AS profileImgUrl, " +
                "DATE_FORMAT(a.created_at, '%Y-%m-%d %H:%i:%s') AS createdAt, " +
                "a.menu_count AS menusCount, " +
                "a.views, " +
                "a.content AS thumbnail " +
                "FROM article a " +
                "JOIN user u ON a.user_id = u.user_id " +
                "WHERE a.user_id = :userId " +
                "AND a.article_id < :startId " +
                "LIMIT :size";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("size", size)
                .addValue("startId", startId);

        return jdbcTemplate.query(sql, namedParameters, (rs, rowNum) -> new UserArticleResponse(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getString("creator"),
                rs.getString("profileImgUrl"),
                rs.getString("createdAt"),
                rs.getInt("menusCount"),
                rs.getInt("views"),
                rs.getString("thumbnail")
        ));
    }

}
