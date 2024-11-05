package guru.qa.niffler.mydata.dao;

import guru.qa.niffler.mydata.entity.user.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDataDao {
    UserEntity create(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findAll();

    void deleteUser(UserEntity user);
}
