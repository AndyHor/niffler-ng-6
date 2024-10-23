package guru.qa.niffler.jupiter.myextensions;

import guru.qa.niffler.mydata.Databases;

public class DatabasesExtension implements SuiteExtension {

    @Override
    public void afterSuite() {
        Databases.closeAllConnections();
    }
}
