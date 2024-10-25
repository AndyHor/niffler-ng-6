package guru.qa.niffler.myservice;

import guru.qa.niffler.myconfig.MyConfig;
import guru.qa.niffler.mydata.entity.spend.SpendEntity;
import guru.qa.niffler.mymodel.SpendJson;
import guru.qa.niffler.mymodel.UserJson;
import guru.qa.niffler.mydata.dao.UserDataDao;
import guru.qa.niffler.mydata.dao.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.mydata.entity.user.UserEntity;

import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.mydata.Databases.transaction;

public class UserdataUserDbClient {

    private static final MyConfig CFG = MyConfig.getInstance();

    public UserJson create(UserJson user) {
        return transaction(connection ->
        {
            return UserJson.fromEntity(new UserdataUserDAOJdbc(connection).create(UserEntity.fromJson(user)));
        }, CFG.spendJdbcUrl(), Connection.TRANSACTION_REPEATABLE_READ);
    }

    public Optional<UserJson> findUserById(UUID id) {
        return transaction(connection ->
        {
            Optional<UserEntity> userEntity = new UserdataUserDAOJdbc(connection).findById(id);
            if (userEntity.isPresent()) {
                return Optional.of(UserJson.fromEntity(userEntity.get()));
            } else return Optional.empty();
        }, CFG.spendJdbcUrl(), Connection.TRANSACTION_REPEATABLE_READ);
    }

    public Optional<UserJson> findUserByUsername(String username) {
        return transaction(connection ->
        {
            Optional<UserEntity> userEntity = new UserdataUserDAOJdbc(connection).findByUsername(username);
            if (userEntity.isPresent()) {
                return Optional.of(UserJson.fromEntity(userEntity.get()));
            } else return Optional.empty();
        }, CFG.spendJdbcUrl(), Connection.TRANSACTION_REPEATABLE_READ);
    }

    public void deleteUser(UserJson user) {
        transaction(connection ->
        {
            UserdataUserDAOJdbc userdataUserDAOJdbc = new UserdataUserDAOJdbc(connection);
            Optional<UserEntity> userEntity = userdataUserDAOJdbc.findById(user.id());
            if (userEntity.isPresent()) {
                userdataUserDAOJdbc.deleteUser(UserEntity.fromJson(user));
            }
        }, CFG.spendJdbcUrl(), Connection.TRANSACTION_REPEATABLE_READ);
    }
}
