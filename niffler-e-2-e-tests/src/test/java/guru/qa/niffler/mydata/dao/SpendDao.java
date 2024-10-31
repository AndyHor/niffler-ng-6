package guru.qa.niffler.mydata.dao;

import guru.qa.niffler.mydata.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {
    SpendEntity create(SpendEntity spend);
    Optional<SpendEntity> findSpend(UUID id);
    List<SpendEntity> findAllByUsername(String username);
    List<SpendEntity> findAll();
    void deleteSpend(SpendEntity spend);
}
