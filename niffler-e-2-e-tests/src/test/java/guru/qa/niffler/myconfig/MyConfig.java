package guru.qa.niffler.myconfig;

public interface MyConfig {

    static MyLocalConfig getInstance() {
        return MyLocalConfig.INSTANCE;
    }

    String frontUrl();

    String authUrl();

    String gatewayUrl();

    String userdataUrl();

    String spendUrl();

    String ghUrl();
}
