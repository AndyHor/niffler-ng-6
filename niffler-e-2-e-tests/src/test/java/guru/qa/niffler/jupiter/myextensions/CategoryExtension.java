package guru.qa.niffler.jupiter.myextensions;

import com.github.jknack.handlebars.internal.lang3.ArrayUtils;
import guru.qa.niffler.jupiter.myannotations.Category;
import guru.qa.niffler.jupiter.myannotations.User;
import guru.qa.niffler.mymodel.CategoryJson;
import guru.qa.niffler.myservice.CategoryDbClient;
import guru.qa.niffler.test.myweb.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;


public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final CategoryDbClient categoryDbClient = new CategoryDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnnotation -> {
                    if (ArrayUtils.isNotEmpty(userAnnotation.categories())) {
                        Category categoryAnnotation = userAnnotation.categories()[0];
                        String categoryName = "";
                        if (categoryAnnotation.name().equals("")) {
                            categoryName = RandomDataUtils.randomCategoryName();
                        } else {
                            categoryName = categoryAnnotation.name();
                        }
                        CategoryJson categoryJson = new CategoryJson(
                                null,
                                categoryName,
                                userAnnotation.username(),
                                false);
                        CategoryJson newCategory = categoryDbClient.createCategory(categoryJson);
                        if (categoryAnnotation.archived()) {
                            CategoryJson archCategory = new CategoryJson(
                                    newCategory.id(),
                                    newCategory.name(),
                                    newCategory.username(),
                                    true
                            );
                            newCategory = categoryDbClient.updateCategory(archCategory);
                        }
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                newCategory);
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (!category.archived()) {
            categoryDbClient.updateCategory(new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true));
        }
    }
}
