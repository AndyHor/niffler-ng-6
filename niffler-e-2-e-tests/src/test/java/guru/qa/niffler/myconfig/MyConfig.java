package guru.qa.niffler.myconfig;

public interface MyConfig {

    static MyLocalConfig getInstance() {
        return MyLocalConfig.INSTANCE;
    }

    String frontUrl();

    String authUrl();
    String authJdbcUrl();

    String gatewayUrl();

    String userdataUrl();
    String userdataJdbcUrl();

    String spendUrl();
    String spendJdbcUrl();

    String currencyJdbcUrl();

    String ghUrl();
}
