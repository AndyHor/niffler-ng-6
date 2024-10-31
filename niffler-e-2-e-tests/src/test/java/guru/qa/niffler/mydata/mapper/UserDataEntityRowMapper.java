package guru.qa.niffler.mydata.mapper;

import guru.qa.niffler.mydata.entity.auth.AuthUserEntity;
import guru.qa.niffler.mydata.entity.user.UserEntity;
import guru.qa.niffler.mymodel.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDataEntityRowMapper implements RowMapper<UserEntity> {

    public static final UserDataEntityRowMapper instance = new UserDataEntityRowMapper();

    private UserDataEntityRowMapper(){

    }

    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(rs.getObject("id", UUID.class));
        userEntity.setUsername(rs.getString("username"));
        userEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        userEntity.setFirstname(rs.getString("firstname"));
        userEntity.setSurname(rs.getString("surname"));
        userEntity.setPhoto(rs.getBytes("photo"));
        userEntity.setPhotoSmall(rs.getBytes("photo_small"));
        userEntity.setFullname(rs.getString("full_name"));
        return userEntity;
    }
}
