package guru.qa.niffler.mydata.dao.impl;

import guru.qa.niffler.myconfig.MyConfig;
import guru.qa.niffler.mydata.dao.AuthAuthorityDao;
import guru.qa.niffler.mydata.entity.auth.AuthorityEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

    private final DataSource dataSource;

    public AuthAuthorityDaoSpringJdbc(DataSource dataSource){
        this.dataSource = dataSource;
    }

    private static final MyConfig CFG = MyConfig.getInstance();

    @Override
    public void create(AuthorityEntity... authorityEntities) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.batchUpdate(
                "insert into authority (user_id, authority) values(?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authorityEntities[i].getUser().getId());
                        ps.setObject(2, authorityEntities[i].getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authorityEntities.length;
                    }
                }
        );
    }
}
