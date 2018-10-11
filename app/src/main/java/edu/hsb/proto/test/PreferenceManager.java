package edu.hsb.proto.test;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.hsb.proto.test.domain.LocationAccuracy;

public class PreferenceManager {

    public static final ExecutorService PREFERENCE_EXECUTOR = Executors.newSingleThreadExecutor();

    public static final String PREF_ACCURACY = "pref_accuracy";
    public static final String PREF_USERNAME = "pref_username";
    public static final String PREF_PASSWORD = "pref_password";

    private static final String DEFAULT_PREF_ACCURACY = LocationAccuracy.HIGH.name();

    private SharedPreferences sharedPreferences;
    private Context context;

    public PreferenceManager(Context context, SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.context = context;
    }

    public LocationAccuracy getLocationAccuracy() {
        return LocationAccuracy.valueOf(sharedPreferences.getString(PREF_ACCURACY, DEFAULT_PREF_ACCURACY));
    }

    public Task setLocationAccuracy(LocationAccuracy locationAccuracy) {
        return apply(sharedPreferences.edit().putString(PREF_ACCURACY, locationAccuracy.name()));
    }

    public String getUsername() {
        return sharedPreferences.getString(PREF_USERNAME, "");
    }

    public Task setUsername(String username) {
        return apply(sharedPreferences.edit().putString(PREF_USERNAME, username));
    }

    public String getPassword() {
        return sharedPreferences.getString(PREF_PASSWORD, "");
    }

    public Task setPassword(String password) {
        return apply(sharedPreferences.edit().putString(PREF_ACCURACY, password));
    }

    private Task<Void> apply(SharedPreferences.Editor editor) {
        return Tasks.call(PREFERENCE_EXECUTOR, () -> {
            editor.commit();
            return null;
        });
    }
}