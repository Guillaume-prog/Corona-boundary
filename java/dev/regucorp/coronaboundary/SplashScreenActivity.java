package dev.regucorp.coronaboundary;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import dev.regucorp.coronaboundary.notifications.NotificationSender;
import dev.regucorp.coronaboundary.permissions.Permission;
import dev.regucorp.coronaboundary.position.LocationHandler;

/**
 * Created by Guillaume ROUSSIN on 25/11/20
 */
public class SplashScreenActivity extends BaseActivity {

    private static final int PERM_CODE = 1234;
    private boolean posReady = false, permAllowed = false;

    LocationHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setTag("SPLASH");

        NotificationSender sender = NotificationSender.getInstance(this);
        sender.setNotifIcon(R.mipmap.ic_launcher);

        permAllowed = Permission.requestPermissions(this, Permission.LOCATION_PERMISSIONS, PERM_CODE);
        if(permAllowed) initPosition();
    }

    private void checkAppReady() {
        if(permAllowed && posReady)
            startActivity(new Intent(this, MainActivity.class));
    }

    private void initPosition() {
        handler = LocationHandler.getInstance();
        handler.init(this, location -> {
            posReady = true;
            log("Position ready");

            checkAppReady();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode != PERM_CODE) return;

        if(Permission.accepted(grantResults)) {
            permAllowed = true;
            log("Position allowed");
            initPosition();
        } else {
            permAllowed = false;
            toast(R.string.splash_noLocation);
        }

    }
}
