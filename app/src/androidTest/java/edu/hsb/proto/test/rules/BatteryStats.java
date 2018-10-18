package edu.hsb.proto.test.rules;

import android.util.Log;

import org.junit.rules.ExternalResource;

import java.io.InputStream;
import java.util.Scanner;

public class BatteryStats extends ExternalResource {

    private static final String TAG = BatteryStats.class.getSimpleName();

    @Override
    public void before() {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("dumpsys", "batterystats", "--reset");
            Process process = builder.start();
            process.waitFor();
        } catch (Exception e) {
            Log.e(TAG, "Unable to reset dumpsys", e);
        }
    }

    @Override
    public void after() {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("dumpsys", "batterystats");
            builder.redirectErrorStream();

            Process process = builder.start();
            Log.w(TAG, process.getInputStream().toString());
            Log.w(TAG, convertStreamToString(process.getInputStream()));
            process.waitFor();

            if (process.exitValue() != 0) {
                throw new Exception("Error taking dumpsys: " + process.exitValue());
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to take dumpsys", e);
        }
    }

    private String convertStreamToString(InputStream is) {
        try {
            return new Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }
}