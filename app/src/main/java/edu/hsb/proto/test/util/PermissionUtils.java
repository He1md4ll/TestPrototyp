package edu.hsb.proto.test.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import edu.hsb.proto.test.R;

public class PermissionUtils {

    public interface IPermissionCallback {
        void onResult(boolean success);
    }

    private static class PermissionReq {
        final Activity activity;
        final String permission;
        final int reqCode;
        final IPermissionCallback callback;

        PermissionReq(Activity activity, String permission, int reqCode, IPermissionCallback callback) {
            this.activity = activity;
            this.permission = permission;
            this.reqCode = reqCode;
            this.callback = callback;
        }
    }

    private static List<PermissionReq> permissionReqList = new ArrayList<>();

    public static boolean hasPermission(Activity activity, String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void checkPermission(Activity activity, String permission,
                                       int reqCode, final IPermissionCallback callback) {
        if (hasPermission(activity, permission)) {
            activity.getWindow().getDecorView().post(() -> callback.onResult(true));
        } else {
            boolean shouldShowReqReason = ActivityCompat
                    .shouldShowRequestPermissionRationale(activity, permission);
            PermissionReq req = new PermissionReq(
                    activity, permission, reqCode, callback);
            if (shouldShowReqReason) {
                showReqReason(req);
            } else {
                reqPermission(req);
            }
        }
    }

    private static void showReqReason(final PermissionReq req) {
        new AlertDialog.Builder(req.activity)
                .setCancelable(false)
                .setMessage(R.string.reason)
                .setPositiveButton(R.string.ok, (dialog, which) -> reqPermission(req))
                .show();
    }

    private static void reqPermission(PermissionReq req) {
        permissionReqList.add(req);
        ActivityCompat.requestPermissions(req.activity, new String[]{req.permission}, req.reqCode);
    }

    private static void showRejectedMsg(final PermissionReq req) {
        new AlertDialog.Builder(req.activity)
                .setCancelable(false)
                .setMessage(R.string.rejected)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    req.callback.onResult(false);
                    permissionReqList.remove(req);
                })
                .setNegativeButton(R.string.change_setting, (dialog, which) ->
                        openAppDetailSetting(req))
                .show();
    }

    private static void openAppDetailSetting(PermissionReq req) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", req.activity.getPackageName(), null);
        intent.setData(uri);
        req.activity.startActivityForResult(intent, req.reqCode);
    }

    public static void onRequestPermissionResult(Activity activity, int requestCode,
                                                 String[] permissions, int[] grantResults) {
        PermissionReq targetReq = null;
        for (PermissionReq req : permissionReqList) {
            if (req.activity.equals(activity)
                    && req.reqCode == requestCode
                    && req.permission.equals(permissions[0])) {
                targetReq = req;
                break;
            }
        }
        if (targetReq != null) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                targetReq.callback.onResult(true);
                permissionReqList.remove(targetReq);
            } else {
                showRejectedMsg(targetReq);
            }
        }
    }

    public static void onActivityResult(Activity activity, int reqCode) {
        PermissionReq targetReq = null;
        for (PermissionReq req : permissionReqList) {
            if (req.activity.equals(activity)
                    && req.reqCode == reqCode) {
                targetReq = req;
                break;
            }
        }
        if (targetReq != null) {
            if (hasPermission(targetReq.activity, targetReq.permission)) {
                targetReq.callback.onResult(true);
            } else {
                targetReq.callback.onResult(false);
            }
            permissionReqList.remove(targetReq);
        }
    }
}