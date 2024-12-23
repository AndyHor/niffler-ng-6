package guru.qa.niffler.jupiter.myextensions;

import guru.qa.niffler.jupiter.myannotations.Spending;
import guru.qa.niffler.jupiter.myannotations.User;
import guru.qa.niffler.mymodel.CategoryJson;
import guru.qa.niffler.mymodel.CurrencyValues;
import guru.qa.niffler.mymodel.SpendJson;
import guru.qa.niffler.myservice.SpendDbClient;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnnotation -> {
                    if (ArrayUtils.isNotEmpty(userAnnotation.spendings())) {
                        Spending spendAnnotation = userAnnotation.spendings()[0];
                        SpendJson spend = new SpendJson(
                                null,
                                new Date(),
                                new CategoryJson(
                                        null,
                                        spendAnnotation.category(),
                                        userAnnotation.username(),
                                        false
                                ),
                                CurrencyValues.RUB,
                                spendAnnotation.amount(),
                                spendAnnotation.description(),
                                userAnnotation.username()
                        );
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                spendDbClient.createSpend(spend)
                        );
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
    }
}
