package guru.qa.niffler.mydata.dao;

import guru.qa.niffler.mydata.entity.spend.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {
    CategoryEntity create(CategoryEntity category);
    CategoryEntity update(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);
    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName);
    List<CategoryEntity> findAllByUsername(String username);
    List<CategoryEntity> findAll();
    void deleteCategory(CategoryEntity categoryEntity);
}
