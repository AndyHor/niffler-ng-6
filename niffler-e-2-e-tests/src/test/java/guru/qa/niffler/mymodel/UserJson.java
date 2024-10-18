package guru.qa.niffler.mymodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.mydata.entity.user.UserEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record UserJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("username")
    String username,
    @JsonProperty("firstname")
    String firstname,
    @JsonProperty("surname")
    String surname,
    @JsonProperty("fullname")
    String fullname,
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("photo")
    byte[] photo,
    @JsonProperty("photoSmall")
    byte[] photoSmall) {

  public static UserJson fromEntity(UserEntity entity) {
    return new UserJson(
        entity.getId(),
        entity.getUsername(),
        entity.getFirstname(),
        entity.getSurname(),
        entity.getFullname(),
        entity.getCurrency(),
        entity.getPhoto() != null && entity.getPhoto().length > 0 ? entity.getPhoto() : null,
        entity.getPhotoSmall() != null && entity.getPhotoSmall().length > 0 ? entity.getPhotoSmall() : null
    );
  }
}
