package guru.qa.niffler.mymodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.mydata.entity.spend.CategoryEntity;

import java.util.UUID;

public record CategoryJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("name")
    String name,
    @JsonProperty("username")
    String username,
    @JsonProperty("archived")
    boolean archived) {

  public static CategoryJson fromEntity(CategoryEntity entity) {
    return new CategoryJson(
        entity.getId(),
        entity.getName(),
        entity.getUsername(),
        entity.isArchived()
    );
  }
}
