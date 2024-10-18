package guru.qa.niffler.myservice;

import guru.qa.niffler.mydata.dao.CategoryDao;
import guru.qa.niffler.mydata.dao.SpendDao;
import guru.qa.niffler.mydata.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.mydata.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.mydata.entity.spend.CategoryEntity;
import guru.qa.niffler.mydata.entity.spend.SpendEntity;
import guru.qa.niffler.mymodel.SpendJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }

        return SpendJson.fromEntity(spendDao.create(spendEntity));
    }

    public Optional<SpendJson> findSpendById(UUID id) {
        Optional<SpendEntity> spendEntity = spendDao.findSpend(id);
        if (spendEntity.isPresent()) {
            return Optional.of(SpendJson.fromEntity(spendEntity.get()));
        } else return Optional.empty();
    }

    public List<SpendJson> findAllSpendsByUsername(String username) {
        List<SpendJson> spendJsons = new ArrayList<>();
        List<SpendEntity> spendEntities = spendDao.findAllByUsername(username);

        if (!spendEntities.isEmpty()) {
            spendEntities.stream().forEach(e -> {
                spendJsons.add(SpendJson.fromEntity(e));
            });
        }

        return spendJsons;
    }

    public void deleteSpend(UUID id) {
        Optional<SpendEntity> spendEntity = spendDao.findSpend(id);
        if (spendEntity.isPresent()) {
            spendDao.deleteSpend(spendEntity.get());
        }
    }

}
