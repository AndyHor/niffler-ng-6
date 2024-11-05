package guru.qa.niffler.mydata.mapper;

import guru.qa.niffler.mydata.entity.spend.CategoryEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CategoryEntityRowMapper implements RowMapper<CategoryEntity> {

    public static final CategoryEntityRowMapper instance = new CategoryEntityRowMapper();

    private CategoryEntityRowMapper(){

    }

    @Override
    public CategoryEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        CategoryEntity result = new CategoryEntity();

        result.setId(resultSet.getObject("id", UUID.class));
        result.setId(resultSet.getObject("id", UUID.class));
        result.setUsername(resultSet.getString("username"));
        result.setName(resultSet.getString("name"));
        result.setArchived(resultSet.getBoolean("archived"));

        return result;
    }
}
