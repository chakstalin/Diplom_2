package api.datagenerator;

import java.util.Random;

public class UserDataGenerator {
    private static final Random RANDOM = new Random();

    public static String generateRandomEmail() {
        int randomInt = RANDOM.nextInt(1_000_000);
        return "user" + randomInt + "@example.com";
    }

    public static String generateRandomPassword() {
        int randomInt = RANDOM.nextInt(1_000_000);
        return "password" + randomInt;
    }

    public static String generateRandomName() {
        int randomInt = RANDOM.nextInt(1_000_000);
        return "name" + randomInt;
    }
}

