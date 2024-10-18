package guru.qa.niffler.mydata.dao.impl;

import guru.qa.niffler.myconfig.MyConfig;
import guru.qa.niffler.mydata.Databases;
import guru.qa.niffler.mydata.dao.CategoryDao;
import guru.qa.niffler.mydata.dao.SpendDao;
import guru.qa.niffler.mydata.entity.spend.CategoryEntity;
import guru.qa.niffler.mydata.entity.spend.SpendEntity;
import guru.qa.niffler.mymodel.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

    private static final MyConfig CFG = MyConfig.getInstance();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    @Override
    public SpendEntity create(SpendEntity spend) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id)" +
                            "VALUES(?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                preparedStatement.setString(1, spend.getUsername());
                preparedStatement.setDate(2, spend.getSpendDate());
                preparedStatement.setString(3, spend.getCurrency().name());
                preparedStatement.setDouble(4, spend.getAmount());
                preparedStatement.setString(5, spend.getDescription());
                preparedStatement.setObject(6, spend.getCategory().getId());

                preparedStatement.executeUpdate();

                final UUID generatedKey;
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedKey = resultSet.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can't find id in ResultSet");
                    }
                }
                spend.setId(generatedKey);

                return spend;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findSpend(UUID id) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM spend WHERE id = ?"
            )) {
                preparedStatement.setObject(1, id);

                preparedStatement.execute();

                try (ResultSet resultSet = preparedStatement.getResultSet()) {
                    if (resultSet.next()) {

                        SpendEntity spendEntity = new SpendEntity();

                        CategoryEntity categoryEntity = categoryDao.findCategoryById(resultSet.getObject("category_id", UUID.class)).get();

                        spendEntity.setId(resultSet.getObject("id", UUID.class));
                        spendEntity.setUsername(resultSet.getString("username"));
                        spendEntity.setSpendDate(resultSet.getDate("spend_date"));
                        spendEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                        spendEntity.setAmount(resultSet.getDouble("amount"));
                        spendEntity.setDescription(resultSet.getString("description"));
                        spendEntity.setCategory(categoryEntity);

                        return Optional.of(spendEntity);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        List<SpendEntity> spendEntities = new ArrayList<>();
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM spend WHERE username = ?"
            )) {
                preparedStatement.setString(1, username);

                preparedStatement.execute();

                try (ResultSet resultSet = preparedStatement.getResultSet()) {
                    while (resultSet.next()) {
                        SpendEntity spendEntity = new SpendEntity();

                        CategoryEntity categoryEntity = categoryDao.findCategoryById(resultSet.getObject("category_id", UUID.class)).get();

                        spendEntity.setId(resultSet.getObject("id", UUID.class));
                        spendEntity.setUsername(resultSet.getString("username"));
                        spendEntity.setSpendDate(resultSet.getDate("spend_date"));
                        spendEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                        spendEntity.setAmount(resultSet.getDouble("amount"));
                        spendEntity.setDescription(resultSet.getString("description"));
                        spendEntity.setCategory(categoryEntity);

                        spendEntities.add(spendEntity);

                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spendEntities;
    }

    @Override
    public void deleteSpend(SpendEntity spend) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from spend where username = ? and id = ?"
            )) {
                preparedStatement.setString(1, spend.getUsername());
                preparedStatement.setObject(2, spend.getId());

                preparedStatement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
