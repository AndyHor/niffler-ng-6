package guru.qa.niffler.mydata.dao.impl;

import guru.qa.niffler.myconfig.MyConfig;
import guru.qa.niffler.mydata.dao.AuthAuthorityDao;
import guru.qa.niffler.mydata.dao.AuthUserDao;
import guru.qa.niffler.mydata.entity.auth.AuthUserEntity;
import guru.qa.niffler.mydata.entity.auth.AuthorityEntity;
import guru.qa.niffler.mydata.entity.spend.CategoryEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {

    private static final MyConfig CFG = MyConfig.getInstance();

    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthUserEntity create(AuthUserEntity authUserEntity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                        "VALUES(?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setString(1, authUserEntity.getUsername());
            preparedStatement.setString(2, authUserEntity.getPassword());
            preparedStatement.setBoolean(3, authUserEntity.getEnabled());
            preparedStatement.setBoolean(4, authUserEntity.getAccountNonExpired());
            preparedStatement.setBoolean(5, authUserEntity.getAccountNonLocked());
            preparedStatement.setBoolean(6, authUserEntity.getCredentialsNonExpired());

            preparedStatement.executeUpdate();

            final UUID generatedKey;
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            authUserEntity.setId(generatedKey);

            return authUserEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM \"user\" where id = ?"
        )) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();

            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {
                    AuthUserEntity authUserEntity = new AuthUserEntity();

                    authUserEntity.setId(resultSet.getObject("id", UUID.class));
                    authUserEntity.setUsername(resultSet.getString("username"));
                    authUserEntity.setPassword(resultSet.getString("password"));
                    authUserEntity.setEnabled(resultSet.getBoolean("enabled"));
                    authUserEntity.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    authUserEntity.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    authUserEntity.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));

                    return Optional.of(authUserEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        List<AuthUserEntity> authUserEntities = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM \"user\""
        )) {

            preparedStatement.execute();

            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                while (resultSet.next()) {
                    AuthUserEntity authUserEntity = new AuthUserEntity();

                    authUserEntity.setId(resultSet.getObject("id", UUID.class));
                    authUserEntity.setUsername(resultSet.getString("username"));
                    authUserEntity.setPassword(resultSet.getString("password"));
                    authUserEntity.setEnabled(resultSet.getBoolean("enabled"));
                    authUserEntity.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    authUserEntity.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    authUserEntity.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));

                    authUserEntities.add(authUserEntity);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authUserEntities;
    }
}
