package guru.qa.niffler.mydata.entity.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
public class AuthorityEntity implements Serializable {
  private UUID id;

  private Authority authority;

  private AuthUserEntity user;
}
