package guru.qa.niffler.myservice;

import guru.qa.niffler.mydata.dao.CategoryDao;
import guru.qa.niffler.mydata.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.mydata.entity.spend.CategoryEntity;
import guru.qa.niffler.mydata.entity.spend.SpendEntity;
import guru.qa.niffler.mymodel.CategoryJson;
import guru.qa.niffler.mymodel.SpendJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDbClient {

    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    public CategoryJson createCategory(CategoryJson categoryJson) {
        return CategoryJson.fromEntity(categoryDao.create(CategoryEntity.fromJson(categoryJson)));
    }

    public CategoryJson updateCategory(CategoryJson categoryJson) {
        return CategoryJson.fromEntity(categoryDao.update(CategoryEntity.fromJson(categoryJson)));
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        Optional<CategoryEntity> categoryEntity = categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName);
        if (categoryEntity.isPresent())
            return Optional.of(CategoryJson.fromEntity(categoryEntity.get()));
        else return Optional.empty();
    }

    public List<CategoryJson> findAllCategoriesByUsername(String username) {
        List<CategoryJson> categoryJsons = new ArrayList<>();
        List<CategoryEntity> categoryEntities = categoryDao.findAllByUsername(username);

        if (!categoryEntities.isEmpty()) {
            categoryEntities.stream().forEach(e -> {
                categoryJsons.add(CategoryJson.fromEntity(e));
            });
        }

        return categoryJsons;
    }

    public void deleteCategory(UUID id) {
        Optional<CategoryEntity> categoryEntity = categoryDao.findCategoryById(id);
        if (categoryEntity.isPresent()) {
            categoryDao.deleteCategory(categoryEntity.get());
        }
    }
}
