package guru.qa.niffler.myservice;

import guru.qa.niffler.myconfig.MyConfig;
import guru.qa.niffler.mydata.Databases;
import guru.qa.niffler.mydata.dao.CategoryDao;
import guru.qa.niffler.mydata.dao.SpendDao;
import guru.qa.niffler.mydata.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.mydata.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.mydata.entity.spend.CategoryEntity;
import guru.qa.niffler.mydata.entity.spend.SpendEntity;
import guru.qa.niffler.mymodel.SpendJson;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.mydata.Databases.transaction;

public class SpendDbClient {

    private static final MyConfig CFG = MyConfig.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection ->
        {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = new CategoryDaoJdbc(connection).create(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
            return SpendJson.fromEntity(new SpendDaoJdbc(connection).create(spendEntity));
        }, CFG.spendJdbcUrl(), Connection.TRANSACTION_REPEATABLE_READ);
    }

    public Optional<SpendJson> findSpendById(UUID id) {
        return transaction(connection ->
        {
            Optional<SpendEntity> spendEntity = new SpendDaoJdbc(connection).findSpend(id);
            if (spendEntity.isPresent()) {
                return Optional.of(SpendJson.fromEntity(spendEntity.get()));
            } else return Optional.empty();
        }, CFG.spendJdbcUrl(), Connection.TRANSACTION_REPEATABLE_READ);
    }

    public List<SpendJson> findAllSpendsByUsername(String username) {
        List<SpendJson> spendJsons = new ArrayList<>();

        List<SpendEntity> spendEntities = transaction(connection ->
        {
            return new SpendDaoJdbc(connection).findAllByUsername(username);
        }, CFG.spendJdbcUrl(), Connection.TRANSACTION_REPEATABLE_READ);

        if (!spendEntities.isEmpty()) {
            spendEntities.stream().forEach(e -> {
                spendJsons.add(SpendJson.fromEntity(e));
            });
        }
        return spendJsons;
    }

    public void deleteSpend(UUID id) {
        transaction(connection ->
        {
            SpendDaoJdbc spendDaoJdbc = new SpendDaoJdbc(connection);
            Optional<SpendEntity> spendEntity = spendDaoJdbc.findSpend(id);
            if (spendEntity.isPresent()) {
                spendDaoJdbc.deleteSpend(spendEntity.get());
            }
        }, CFG.spendJdbcUrl(), Connection.TRANSACTION_REPEATABLE_READ);
    }

}
