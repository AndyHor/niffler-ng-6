package guru.qa.niffler.mydata.dao.impl;

import guru.qa.niffler.myconfig.MyConfig;
import guru.qa.niffler.mydata.Databases;
import guru.qa.niffler.mydata.dao.CategoryDao;
import guru.qa.niffler.mydata.entity.spend.CategoryEntity;
import guru.qa.niffler.mydata.entity.spend.SpendEntity;
import guru.qa.niffler.mymodel.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoJdbc implements CategoryDao {

    private static final MyConfig CFG = MyConfig.getInstance();

    private final Connection connection;

    public CategoryDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public CategoryEntity create(CategoryEntity category) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO category (username, name, archived)" +
                        "VALUES(?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setString(1, category.getUsername());
            preparedStatement.setString(2, category.getName());
            preparedStatement.setBoolean(3, category.isArchived());

            preparedStatement.executeUpdate();

            final UUID generatedKey;
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            category.setId(generatedKey);

            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity update(CategoryEntity category) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE category set name = ?, archived = ? where username = ? and id = ?",
                Statement.RETURN_GENERATED_KEYS
        )) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.setBoolean(2, category.isArchived());
            preparedStatement.setString(3, category.getUsername());
            preparedStatement.setObject(4, category.getId());

            preparedStatement.executeUpdate();

            final UUID generatedKey;
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            category.setId(generatedKey);

            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM category WHERE id = ?"
        )) {
            preparedStatement.setObject(1, id);

            preparedStatement.execute();

            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {

                    CategoryEntity categoryEntity = new CategoryEntity();

                    categoryEntity.setId(resultSet.getObject("id", UUID.class));
                    categoryEntity.setUsername(resultSet.getString("username"));
                    categoryEntity.setName(resultSet.getString("name"));
                    categoryEntity.setArchived(resultSet.getBoolean("archived"));
                    return Optional.of(categoryEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM category WHERE username = ? and name = ?"
        )) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, categoryName);
            preparedStatement.execute();

            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {

                    CategoryEntity categoryEntity = new CategoryEntity();

                    categoryEntity.setId(resultSet.getObject("id", UUID.class));
                    categoryEntity.setUsername(resultSet.getString("username"));
                    categoryEntity.setName(resultSet.getString("name"));
                    categoryEntity.setArchived(resultSet.getBoolean("archived"));
                    return Optional.of(categoryEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM category WHERE username = ?"
        )) {
            preparedStatement.setString(1, username);

            preparedStatement.execute();

            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                while (resultSet.next()) {
                    CategoryEntity categoryEntity = new CategoryEntity();

                    categoryEntity.setId(resultSet.getObject("id", UUID.class));
                    categoryEntity.setUsername(resultSet.getString("username"));
                    categoryEntity.setName(resultSet.getString("name"));
                    categoryEntity.setArchived(resultSet.getBoolean("archived"));

                    categoryEntities.add(categoryEntity);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categoryEntities;
    }

    @Override
    public void deleteCategory(CategoryEntity categoryEntity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from category where username = ? and id = ?"
        )) {
            preparedStatement.setString(1, categoryEntity.getUsername());
            preparedStatement.setObject(2, categoryEntity.getId());

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
