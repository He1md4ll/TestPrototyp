package edu.hsb.proto.test.service.impl;

import android.text.TextUtils;

import edu.hsb.proto.test.service.ILoginService;
import edu.hsb.proto.test.util.CryptoUtils;

public class FakeLoginService implements ILoginService {

    private static final String SUPER_SECRET_KEY = "3jx2rf*dagf3bh#utf$f234!";
    private static final String RIGHT_USERNAME = "martin";
    private static final String RIGHT_PASSWORD = "Start123*";


    @Override
    public boolean login(String username, String passwordHash, int rounds, Boolean encryption) {
        return  !TextUtils.isEmpty(username) &&
                !TextUtils.isEmpty(passwordHash) &&
                RIGHT_USERNAME.equals(username) &&
                hash(RIGHT_PASSWORD, rounds, encryption).equals(passwordHash);
    }

    @Override
    public String hash(String stringToHash, int rounds, Boolean encryption) {
        String result = stringToHash;
        for (int i = 0; i < rounds; i++) {
            if (encryption) {
                result = CryptoUtils.encryptHmacSHA512ToString(result, SUPER_SECRET_KEY);
            } else {
                result = CryptoUtils.encryptSHA512ToString(result);
            }
        }
        return result;
    }
}