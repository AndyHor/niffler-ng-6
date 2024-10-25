package guru.qa.niffler.mydata.dao.impl;

import guru.qa.niffler.myconfig.MyConfig;
import guru.qa.niffler.mydata.dao.AuthAuthorityDao;
import guru.qa.niffler.mydata.dao.AuthUserDao;
import guru.qa.niffler.mydata.entity.auth.AuthUserEntity;
import guru.qa.niffler.mydata.entity.auth.AuthorityEntity;

import java.sql.*;
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
}
