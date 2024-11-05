package guru.qa.niffler.mydata.dao.impl;

import guru.qa.niffler.myconfig.MyConfig;
import guru.qa.niffler.mydata.dao.UserDataDao;
import guru.qa.niffler.mydata.entity.auth.AuthUserEntity;
import guru.qa.niffler.mydata.entity.user.UserEntity;
import guru.qa.niffler.mydata.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.mydata.mapper.UserDataEntityRowMapper;
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

public class UserdataUserDAOSpringJdbc implements UserDataDao {

    private final DataSource dataSource;

    public UserdataUserDAOSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public UserEntity create(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name)" +
                            "VALUES(?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getCurrency().name());
            preparedStatement.setString(3, user.getFirstname());
            preparedStatement.setString(4, user.getSurname());
            preparedStatement.setBytes(5, user.getPhoto());
            preparedStatement.setBytes(6, user.getPhotoSmall());
            preparedStatement.setString(7, user.getFullname());

            return preparedStatement;
        }, keyHolder);

        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        user.setId(generatedKey);
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "select * from \"user\" where id = ?",
                        UserDataEntityRowMapper.instance,
                        id
                )
        );
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\"  WHERE username = ?",
                        UserDataEntityRowMapper.instance,
                        username
                )
        );
    }

    @Override
    public List<UserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(
                "SELECT * FROM \"user\"",
                UserDataEntityRowMapper.instance
        );
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
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from \"user\" where id = ?"
            );
            preparedStatement.setObject(1, user.getId());

            return preparedStatement;
        });
    }
}
