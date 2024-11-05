package guru.qa.niffler.mydata.dao;

import guru.qa.niffler.mydata.entity.auth.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {
    AuthUserEntity create(AuthUserEntity authUserEntity);

    Optional<AuthUserEntity> findById(UUID id);
    List<AuthUserEntity> findAll();
}
