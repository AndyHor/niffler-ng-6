package guru.qa.niffler.mydata.dao.impl;

import guru.qa.niffler.myconfig.MyConfig;
import guru.qa.niffler.mydata.dao.CategoryDao;
import guru.qa.niffler.mydata.entity.spend.CategoryEntity;
import guru.qa.niffler.mydata.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.mydata.mapper.CategoryEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoSpringJdbc implements CategoryDao {

    private final DataSource dataSource;

    public CategoryDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public CategoryEntity create(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO category (username, name, archived)" +
                            "VALUES(?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, category.getUsername());
            preparedStatement.setString(2, category.getName());
            preparedStatement.setBoolean(3, category.isArchived());

            return preparedStatement;

        }, keyHolder);

        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        category.setId(generatedKey);

        return category;
    }

    @Override
    public CategoryEntity update(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE category set name = ?, archived = ? where id = ?",
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, category.getName());
            preparedStatement.setBoolean(2, category.isArchived());
            preparedStatement.setObject(3, category.getId());

            return preparedStatement;

        }, keyHolder);

            final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
            category.setId(generatedKey);

            return category;
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM category WHERE id = ?",
                        CategoryEntityRowMapper.instance,
                        id
                )
        );
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM category WHERE username = ? and name = ?",
                        CategoryEntityRowMapper.instance,
                        username, categoryName
                )
        );
    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        List<CategoryEntity> categoryEntities = new ArrayList<>();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        categoryEntities = jdbcTemplate.query(
                "SELECT * FROM category WHERE username = ?",
                CategoryEntityRowMapper.instance,
                username
        );

        return categoryEntities;
    }

    @Override
    public List<CategoryEntity> findAll() {
        List<CategoryEntity> categoryEntities = new ArrayList<>();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        categoryEntities = jdbcTemplate.query(
                "SELECT * FROM category",
                CategoryEntityRowMapper.instance
        );

        return categoryEntities;
    }

    @Override
    public void deleteCategory(CategoryEntity categoryEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM category WHERE username = ? and id = ?"
            );
            preparedStatement.setString(1, categoryEntity.getUsername());
            preparedStatement.setObject(2, categoryEntity.getId());

            return preparedStatement;
        });
    }
}
