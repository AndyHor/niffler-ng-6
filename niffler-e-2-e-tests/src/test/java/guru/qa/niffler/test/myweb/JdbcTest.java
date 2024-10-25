package guru.qa.niffler.test.myweb;

import guru.qa.niffler.mydata.dao.UserDataDao;
import guru.qa.niffler.mymodel.CategoryJson;
import guru.qa.niffler.mymodel.CurrencyValues;
import guru.qa.niffler.mymodel.SpendJson;
import guru.qa.niffler.mymodel.UserJson;
import guru.qa.niffler.myservice.CategoryDbClient;
import guru.qa.niffler.myservice.SpendDbClient;
import guru.qa.niffler.myservice.UserDbClient;
import guru.qa.niffler.myservice.UserdataUserDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcTest {



    @Test
    void daotest(){

        //SpendJson[id=ad50d924-8d53-11ef-bc05-0242ac110002, spendDate=2024-10-18, category=CategoryJson[id=ad4aef96-8d53-11ef-9d13-0242ac110002, name=test-cat-name4, username=duck, archived=false], currency=RUB, amount=23.0, description=test desc, username=duck]


        //CategoryDbClient categoryDbClient = new

/*        UserdataUserDbClient userdataUserDbClient = new UserdataUserDbClient();

       Optional<UserJson> userJson = userdataUserDbClient.findUserByUsername("duck");
                *//*userdataUserDbClient.create( new UserJson(
                null,
                "aab",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null
        ));
*//*
        System.out.println(userJson.get());*/
/*        SpendDbClient spendDbClient = new SpendDbClient();

        CategoryDbClient categoryDbClient = new CategoryDbClient();

        List<CategoryJson> categoryJsons = categoryDbClient.findAllCategoriesByUsername("duck");
        categoryJsons.stream().forEach(System.out::println);

        CategoryJson categoryJson = categoryDbClient.findCategoryByUsernameAndCategoryName("duck", "Bread").get();
        System.out.println(categoryJson);*/

        //categoryDbClient.deleteCategory(UUID.fromString("a657746e-8d5f-11ef-b017-0242ac110002"));
/*        CategoryJson categoryJson = categoryDbClient.createCategory(new CategoryJson(
                null,
                "test-cat-name6",
                "duck",
                false));*/

        //System.out.println(categoryJson);

        //categoryDbClient.deleteCategory(categoryJson.id());

        //List<SpendJson> spendJsons = spendDbClient.findAllSpendsByUsername("duck");
        //spendJsons.stream().forEach(System.out::println);
        //UUID id = UUID.fromString("ad50d924-8d53-11ef-bc05-0242ac110002");

        //spendDbClient.deleteSpend(id);
        //Optional<SpendJson> spend = spendDbClient.findSpendById(id);
        //System.out.println(spend.get());
/*        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
            new SpendJson(
                    null,
                    new Date(),
                    new CategoryJson(
                            null,
                            "test-cat-name7",
                            "duck2",
                            false

                    ),
                    CurrencyValues.RUB,
                    23.0,
                    "test desc",
                    "duck2"
            )
        );

        System.out.println(spend);*/

        UserDbClient userDbClient = new UserDbClient();

        UserJson user = userDbClient.create(new UserJson(
                null,
                "cat4",
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null));

        System.out.println(user);
    }
}
