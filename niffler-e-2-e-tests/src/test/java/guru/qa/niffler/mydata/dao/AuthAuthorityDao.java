package guru.qa.niffler.mydata.dao;

import guru.qa.niffler.mydata.entity.auth.AuthorityEntity;

public interface AuthAuthorityDao {
    void create(AuthorityEntity... authorityEntities);
}
