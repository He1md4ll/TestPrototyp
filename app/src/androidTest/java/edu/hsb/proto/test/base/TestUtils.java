package edu.hsb.proto.test.base;

import java.util.Random;

public class TestUtils {

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm*!-+#$%&<>_";

    public static String randomString(int maxLength) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(maxLength);
        for (int i = 0; i < randomLength; i++){
            randomStringBuilder.append(ALLOWED_CHARACTERS.charAt(generator.nextInt(ALLOWED_CHARACTERS.length())));
        }
        return randomStringBuilder.toString();
    }

    public static int randomInt(int maxInt) {
        return new Random().nextInt(maxInt + 1);
    }
}