package edu.hsb.proto.test;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import edu.hsb.proto.test.util.PermissionUtils;

public abstract class PermissionBase extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQ_CODE = 200;

    public void checkLocation(PermissionUtils.IPermissionCallback callback) {
        PermissionUtils.checkPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_PERMISSION_REQ_CODE,
                callback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (LOCATION_PERMISSION_REQ_CODE == requestCode) {
            PermissionUtils.onActivityResult(this, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionResult(this, requestCode, permissions, grantResults);
    }
}