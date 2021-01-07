package dev.regucorp.coronaboundary.permissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * Created by Guillaume ROUSSIN on 12/08/20
 */
public class Permission {

    public static final String TAG = "PermissionChecker";

    // Permission List
    public static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    /* Permission checking */

    public static boolean requestPermissions(AppCompatActivity act, String[] permissions, int requestCode) {
        // Check the permissions
        boolean permissionsGranted = true;

        for(String perm : permissions) {
            Log.d(TAG, "permissionCheck: " + perm + " - " + checkPermission(act, perm));
            if(!checkPermission(act, perm)) permissionsGranted = false;
        }

        if(!permissionsGranted) request(act, permissions, requestCode);

        return permissionsGranted;
    }

    private static boolean checkPermission(AppCompatActivity act, String perm) {
        return (ActivityCompat.checkSelfPermission(act, perm) == PackageManager.PERMISSION_GRANTED);
    }

    private static void request(AppCompatActivity act, String[] permissions, int requestCode) {
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)
            return;

        Log.d(TAG, "permissionCheck: requesting permissions");

        act.requestPermissions(permissions, requestCode);
    }

    /* Async check of returned permissions */

    public static boolean accepted(int results[]) {
        if(results.length == 0)
            return false;

        for(int res : results)
            if(res != PackageManager.PERMISSION_GRANTED) return false;

        return true;
    }
}
