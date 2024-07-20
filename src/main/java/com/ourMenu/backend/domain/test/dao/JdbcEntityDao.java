package com.ourMenu.backend.domain.test.dao;

import com.ourMenu.backend.domain.test.domain.JdbcEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcEntityDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void save(String name) {
        String sql = "INSERT INTO Jdbc_entity (name) VALUES (:name)";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", name);
        jdbcTemplate.update(sql, param);
    }

    public JdbcEntity findById(Long jdbc_entity_id) {
        String sql = "SELECT * from Jdbc_entity where id=:jdbc_entity_id";
        Map<String, Object> param = Map.of("jdbc_entity_id", jdbc_entity_id);
        return jdbcTemplate.queryForObject(sql, param, (rs, rowNum) -> new JdbcEntity(
                Long.parseLong(rs.getString("id")),
                rs.getString("name")
        ));
    }

}
