package guru.qa.niffler.myservice;

import guru.qa.niffler.mydata.entity.spend.SpendEntity;
import guru.qa.niffler.mymodel.SpendJson;
import guru.qa.niffler.mymodel.UserJson;
import guru.qa.niffler.mydata.dao.UserDataDao;
import guru.qa.niffler.mydata.dao.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.mydata.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

public class UserdataUserDbClient {

    UserDataDao userDataDao = new UserdataUserDAOJdbc();

    public UserJson create(UserJson user) {
        UserEntity userEntity = UserEntity.fromJson(user);
        return UserJson.fromEntity(userDataDao.create(userEntity));
    }

    public Optional<UserJson> findUserById(UUID id) {
        Optional<UserEntity> userEntity = userDataDao.findById(id);
        if (userEntity.isPresent()) {
            return Optional.of(UserJson.fromEntity(userEntity.get()));
        } else return Optional.empty();
    }

    public Optional<UserJson> findUserByUsername(String username) {
        Optional<UserEntity> userEntity = userDataDao.findByUsername(username);
        if (userEntity.isPresent()) {
            return Optional.of(UserJson.fromEntity(userEntity.get()));
        } else return Optional.empty();
    }

    public void deleteUser(UserJson user) {
        Optional<UserEntity> userEntity = userDataDao.findById(user.id());
        if (userEntity.isPresent()) {
            userDataDao.deleteUser(UserEntity.fromJson(user));
        }
    }
}
