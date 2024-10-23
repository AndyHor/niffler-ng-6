package guru.qa.niffler.myservice;

import guru.qa.niffler.myconfig.MyConfig;
import guru.qa.niffler.mydata.Databases;
import guru.qa.niffler.mydata.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.mydata.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.mydata.dao.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.mydata.entity.auth.AuthUserEntity;
import guru.qa.niffler.mydata.entity.auth.Authority;
import guru.qa.niffler.mydata.entity.auth.AuthorityEntity;
import guru.qa.niffler.mydata.entity.user.UserEntity;
import guru.qa.niffler.mymodel.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.util.Arrays;

import static guru.qa.niffler.mydata.Databases.xaTransaction;

import guru.qa.niffler.mydata.Databases.XaFunction;

public class UserDbClient {

    private static final MyConfig CFG = MyConfig.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserJson create(UserJson user) {
        return UserJson.fromEntity(
                xaTransaction(Connection.TRANSACTION_REPEATABLE_READ,
                        new XaFunction<>(
                                connection ->
                                {
                                    AuthUserEntity authUserEntity = new AuthUserEntity();
                                    authUserEntity.setUsername(user.username());
                                    authUserEntity.setPassword(pe.encode("12345"));
                                    authUserEntity.setEnabled(true);
                                    authUserEntity.setAccountNonExpired(true);
                                    authUserEntity.setAccountNonLocked(true);
                                    authUserEntity.setCredentialsNonExpired(true);
                                    new AuthUserDaoJdbc(connection).create(authUserEntity);
                                    new AuthAuthorityDaoJdbc(connection).create(
                                            Arrays.stream(Authority.values())
                                                    .map(a -> {
                                                                AuthorityEntity authorityEntity = new AuthorityEntity();
                                                                authorityEntity.setUser(authUserEntity);
                                                                authorityEntity.setAuthority(a);
                                                                return authorityEntity;
                                                            }
                                                    )
                                                    .toArray(AuthorityEntity[]::new));
                                    return null;
                                }, CFG.authJdbcUrl()),
                        new XaFunction<>(
                                connection ->
                                {
                                    return new UserdataUserDAOJdbc(connection).create(UserEntity.fromJson(user));

                                }, CFG.userdataJdbcUrl())));

    }
}
