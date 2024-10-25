package guru.qa.niffler.myservice;

import guru.qa.niffler.myconfig.MyConfig;
import guru.qa.niffler.mydata.Databases;
import guru.qa.niffler.mydata.dao.CategoryDao;
import guru.qa.niffler.mydata.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.mydata.entity.spend.CategoryEntity;
import guru.qa.niffler.mydata.entity.spend.SpendEntity;
import guru.qa.niffler.mymodel.CategoryJson;
import guru.qa.niffler.mymodel.SpendJson;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.mydata.Databases.transaction;

public class CategoryDbClient {

    private static final MyConfig CFG = MyConfig.getInstance();

    public CategoryJson createCategory(CategoryJson categoryJson) {
        return transaction(connection ->
        {
            return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(CategoryEntity.fromJson(categoryJson)));

        }, CFG.spendJdbcUrl(), Connection.TRANSACTION_REPEATABLE_READ);
    }

    public CategoryJson updateCategory(CategoryJson categoryJson) {
        return transaction(connection ->
        {
            return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).update(CategoryEntity.fromJson(categoryJson)));
        }, CFG.spendJdbcUrl(), Connection.TRANSACTION_REPEATABLE_READ);
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        Optional<CategoryEntity> categoryEntity = transaction(connection ->
        {
            return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, categoryName);
        }, CFG.spendJdbcUrl(), Connection.TRANSACTION_REPEATABLE_READ);

        if (categoryEntity.isPresent())
            return Optional.of(CategoryJson.fromEntity(categoryEntity.get()));
        else return Optional.empty();
    }

    public List<CategoryJson> findAllCategoriesByUsername(String username) {
        List<CategoryJson> categoryJsons = new ArrayList<>();
        List<CategoryEntity> categoryEntities;

        categoryEntities = transaction(connection ->
        {
            return new CategoryDaoJdbc(connection).findAllByUsername(username);
        }, CFG.spendJdbcUrl(), Connection.TRANSACTION_REPEATABLE_READ);

        categoryEntities.stream().forEach(e -> {
            categoryJsons.add(CategoryJson.fromEntity(e));
        });

        return categoryJsons;
    }

    public void deleteCategory(UUID id) {
        transaction(connection ->
        {
            CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc(connection);
            Optional<CategoryEntity> categoryEntity = categoryDaoJdbc.findCategoryById(id);
            if (categoryEntity.isPresent()) {
                categoryDaoJdbc.deleteCategory(categoryEntity.get());
            }
        }, CFG.spendJdbcUrl(), Connection.TRANSACTION_REPEATABLE_READ);
    }

}
