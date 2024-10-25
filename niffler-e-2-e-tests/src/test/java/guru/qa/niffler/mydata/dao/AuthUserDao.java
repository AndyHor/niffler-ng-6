package guru.qa.niffler.mydata.dao;

import guru.qa.niffler.mydata.entity.auth.AuthUserEntity;

public interface AuthUserDao {
    AuthUserEntity create(AuthUserEntity authUserEntity);
}
