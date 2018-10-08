package edu.hsb.proto.test.service.impl;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import edu.hsb.proto.test.connection.ConnectionManager;
import edu.hsb.proto.test.domain.User;
import edu.hsb.proto.test.service.ILoginService;
import edu.hsb.proto.test.util.CryptoUtils;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class DefaultLoginService implements ILoginService {

    private static final String TAG = DefaultLoginService.class.getSimpleName();
    private static final String SUPER_SECRET_KEY = "3jx2rf*dagf3bh#utf$f234!";
    private static final String RIGHT_USERNAME = "martin";
    private static final String RIGHT_PASSWORD = "Start123*";

    private ConnectionManager connectionManager;

    public DefaultLoginService(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public boolean loginOnline(String username, String passwordHash) {
        boolean notEmpty = !TextUtils.isEmpty(username) && !TextUtils.isEmpty(passwordHash);
        boolean requestResult = Boolean.FALSE;
        try {
            final Response<ResponseBody> response = connectionManager.getReqresTestApi()
                    .login(new User(username, passwordHash))
                    .execute();
            requestResult = response.isSuccessful();
        } catch (IOException e) {
            Log.w(TAG, "Error while executing request: " + e.getMessage(), e);
        }
        return notEmpty && requestResult;
    }

    @Override
    public boolean loginOffline(String username, String passwordHash, int rounds, Boolean encryption) {
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