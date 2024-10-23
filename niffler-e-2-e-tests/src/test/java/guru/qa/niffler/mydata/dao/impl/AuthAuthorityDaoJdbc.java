package guru.qa.niffler.mydata.dao.impl;

import guru.qa.niffler.myconfig.MyConfig;
import guru.qa.niffler.mydata.dao.AuthAuthorityDao;
import guru.qa.niffler.mydata.dao.CategoryDao;
import guru.qa.niffler.mydata.entity.auth.AuthorityEntity;
import guru.qa.niffler.mydata.entity.spend.CategoryEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private static final MyConfig CFG = MyConfig.getInstance();

    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AuthorityEntity... authorityEntities) {

        for (AuthorityEntity authorityEntity : authorityEntities) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO authority (user_id, authority)" +
                            "VALUES(?, ?)"
            )) {
                preparedStatement.setObject(1, authorityEntity.getUser().getId());
                preparedStatement.setObject(2, authorityEntity.getAuthority().name());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
