package guru.qa.niffler.test.myweb.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    public static String randomUsername() {
        return faker.name().username();
    }

    public static String randomName() {
        return faker.name().name();
    }

    public static String randomSurname() {
        return faker.name().lastName();
    }

    public static String randomCategoryName() {
        return faker.commerce().productName();
    }

    public static String randomSentence(int wordsCount) {
        StringBuffer sb = new StringBuffer();
        if (wordsCount > 0) {
            for (int i = 1; i <= wordsCount; i++) {
                int rc = faker.random().nextInt(1, 5);
                switch (rc) {
                    case 1:
                        sb.append(faker.address().city()).append(" ");
                        break;

                    case 2:
                        sb.append(faker.job().position()).append(" ");
                        break;

                    case 3:
                        sb.append(faker.food().sushi()).append(" ");
                        break;

                    case 4:
                        sb.append(faker.esports().game()).append(" ");
                        break;

                    case 5:
                        sb.append(faker.music().instrument()).append(" ");
                        break;
                }
            }
            return sb.toString();
        }
        return "";
    }
}
