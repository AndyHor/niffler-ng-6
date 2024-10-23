package guru.qa.niffler.mydata.dao.impl;

import guru.qa.niffler.myconfig.MyConfig;
import guru.qa.niffler.mydata.dao.UserDataDao;
import guru.qa.niffler.mydata.entity.user.UserEntity;
import guru.qa.niffler.mymodel.CurrencyValues;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserDAOJdbc implements UserDataDao {

    private static final MyConfig CFG = MyConfig.getInstance();

    private final Connection connection;

    public UserdataUserDAOJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name)" +
                        "VALUES(?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getCurrency().name());
            preparedStatement.setString(3, user.getFirstname());
            preparedStatement.setString(4, user.getSurname());
            preparedStatement.setBytes(5, user.getPhoto());
            preparedStatement.setBytes(6, user.getPhotoSmall());
            preparedStatement.setString(7, user.getFullname());

            preparedStatement.executeUpdate();

            final UUID generatedKey;
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            user.setId(generatedKey);

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM \"user\"  WHERE id = ?"
        )) {
            preparedStatement.setObject(1, id);

            preparedStatement.execute();

            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {

                    UserEntity userEntity = new UserEntity();

                    setParamsToUserEntity(userEntity, resultSet);

                    return Optional.of(userEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM \"user\"  WHERE username = ?"
        )) {
            preparedStatement.setObject(1, username);

            preparedStatement.execute();

            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {

                    UserEntity userEntity = new UserEntity();

                    setParamsToUserEntity(userEntity, resultSet);

                    return Optional.of(userEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void setParamsToUserEntity(UserEntity userEntity, ResultSet resultSet) throws SQLException {
        userEntity.setId(resultSet.getObject("id", UUID.class));
        userEntity.setUsername(resultSet.getString("username"));
        userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
        userEntity.setFirstname(resultSet.getString("firstname"));
        userEntity.setSurname(resultSet.getString("surname"));
        userEntity.setPhoto(resultSet.getBytes("photo"));
        userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));
        userEntity.setFullname(resultSet.getString("full_name"));
    }

    @Override
    public void deleteUser(UserEntity user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from \"user\" where username = ? and id = ?"
        )) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setObject(2, user.getId());

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
