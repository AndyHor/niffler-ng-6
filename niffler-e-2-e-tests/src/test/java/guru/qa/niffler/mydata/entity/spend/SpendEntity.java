package guru.qa.niffler.mydata.entity.spend;

import guru.qa.niffler.mymodel.CurrencyValues;
import guru.qa.niffler.mymodel.SpendJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
public class SpendEntity implements Serializable {
  private UUID id;
  private String username;
  private CurrencyValues currency;
  private Date spendDate;
  private Double amount;
  private String description;
  private guru.qa.niffler.mydata.entity.spend.CategoryEntity category;

  public static SpendEntity fromJson(SpendJson json){
    SpendEntity spendEntity = new SpendEntity();
    spendEntity.setId(json.id());
    spendEntity.setUsername(json.username());
    spendEntity.setCurrency(json.currency());
    spendEntity.setSpendDate(new java.sql.Date(json.spendDate().getTime()));
    spendEntity.setAmount(json.amount());
    spendEntity.setDescription(json.description());
    spendEntity.setCategory(
            CategoryEntity.fromJson(
                    json.category()
            )
    );

    return spendEntity;
  }
}
