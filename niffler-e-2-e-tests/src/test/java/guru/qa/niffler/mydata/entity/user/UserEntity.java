package guru.qa.niffler.mydata.entity.user;

import java.io.Serializable;

import guru.qa.niffler.mymodel.CurrencyValues;
import guru.qa.niffler.mymodel.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserEntity implements Serializable {
    private UUID id;
    private String username;
    private String firstname;
    private String surname;
    private String fullname;
    private CurrencyValues currency;
    private byte[] photo;
    private byte[] photoSmall;

    public static UserEntity fromJson(UserJson userJson){
        UserEntity userEntity = new UserEntity();

        userEntity.setId(userJson.id());
        userEntity.setUsername(userJson.username());
        userEntity.setFirstname(userJson.firstname());
        userEntity.setSurname(userJson.surname());
        userEntity.setFullname(userJson.fullname());
        userEntity.setCurrency(userJson.currency());
        userEntity.setPhoto(userJson.photo());
        userEntity.setPhotoSmall(userJson.photoSmall());

        return userEntity;
    }
}
