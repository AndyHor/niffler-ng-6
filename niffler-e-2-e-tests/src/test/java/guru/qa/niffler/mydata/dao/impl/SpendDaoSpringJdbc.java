package guru.qa.niffler.mydata.dao.impl;

import guru.qa.niffler.myconfig.MyConfig;
import guru.qa.niffler.mydata.dao.SpendDao;
import guru.qa.niffler.mydata.entity.spend.CategoryEntity;
import guru.qa.niffler.mydata.entity.spend.SpendEntity;
import guru.qa.niffler.mydata.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.mydata.mapper.SpendEntityRowMapper;
import guru.qa.niffler.mymodel.CurrencyValues;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoSpringJdbc implements SpendDao {

    private final DataSource dataSource;

    public SpendDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public SpendEntity create(SpendEntity spend) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id)" +
                            "VALUES(?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, spend.getUsername());
            preparedStatement.setDate(2, spend.getSpendDate());
            preparedStatement.setString(3, spend.getCurrency().name());
            preparedStatement.setDouble(4, spend.getAmount());
            preparedStatement.setString(5, spend.getDescription());
            preparedStatement.setObject(6, spend.getCategory().getId());

            return preparedStatement;
        }, keyHolder);

        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        spend.setId(generatedKey);
        return spend;
    }

    @Override
    public Optional<SpendEntity> findSpend(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        SpendEntity spendEntity = jdbcTemplate.queryForObject(
                "SELECT * FROM spend WHERE id = ?",
                SpendEntityRowMapper.instance,
                id
        );

        CategoryEntity categoryEntity = jdbcTemplate.queryForObject(
                "SELECT * FROM category WHERE id = ?",
                CategoryEntityRowMapper.instance,
                spendEntity.getCategory().getId()
        );

        spendEntity.setCategory(categoryEntity);

        return Optional.of(spendEntity);
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        List<SpendEntity> spendEntities = new ArrayList<>();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        spendEntities = jdbcTemplate.query(
                "SELECT * FROM spend WHERE username = ?",
                SpendEntityRowMapper.instance,
                username
        );

        for (SpendEntity spend: spendEntities){
            CategoryEntity categoryEntity = jdbcTemplate.queryForObject(
                    "SELECT * FROM category WHERE id = ?",
                    CategoryEntityRowMapper.instance,
                    spend.getCategory().getId()
            );
            spend.setCategory(categoryEntity);
        }

        return spendEntities;
    }

    @Override
    public List<SpendEntity> findAll() {
        List<SpendEntity> spendEntities = new ArrayList<>();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        spendEntities = jdbcTemplate.query(
                "SELECT * FROM spend",
                SpendEntityRowMapper.instance
        );

        for (SpendEntity spend: spendEntities){
            CategoryEntity categoryEntity = jdbcTemplate.queryForObject(
                    "SELECT * FROM category WHERE id = ?",
                    CategoryEntityRowMapper.instance,
                    spend.getCategory().getId()
            );
            spend.setCategory(categoryEntity);
        }

        return spendEntities;
    }

    @Override
    public void deleteSpend(SpendEntity spend) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from spend where id = ?"
            );

            preparedStatement.setObject(1, spend.getId());

            return preparedStatement;
        });
    }
}
