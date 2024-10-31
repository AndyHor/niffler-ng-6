package guru.qa.niffler.mydata.mapper;

import guru.qa.niffler.mydata.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.mydata.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.mydata.entity.spend.CategoryEntity;
import guru.qa.niffler.mydata.entity.spend.SpendEntity;
import guru.qa.niffler.mymodel.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

    public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();

    private SpendEntityRowMapper(){

    }

    @Override
    public SpendEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        SpendEntity spendEntity = new SpendEntity();

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(resultSet.getObject("category_id", UUID.class));

        spendEntity.setId(resultSet.getObject("id", UUID.class));
        spendEntity.setUsername(resultSet.getString("username"));
        spendEntity.setSpendDate(resultSet.getDate("spend_date"));
        spendEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
        spendEntity.setAmount(resultSet.getDouble("amount"));
        spendEntity.setDescription(resultSet.getString("description"));
        spendEntity.setCategory(categoryEntity);

        return spendEntity;
    }
}
