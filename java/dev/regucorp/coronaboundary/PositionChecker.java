package dev.regucorp.coronaboundary;

import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import dev.regucorp.coronaboundary.permissions.Permission;
import dev.regucorp.coronaboundary.position.LocationHandler;

/**
 * Created by Guillaume ROUSSIN on 07/12/20
 */
public class PositionChecker extends BaseActivity {

    public static final int THRESHOLD_DISTANCE = 20;

    // Views
    private TextView latitudeText, longitudeText, addressText, distanceText;
    private Button homeBtn;

    // Location Callback;
    private LocationHandler handler;
    private Location homeLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.positioncheck);

        initViews();

        boolean permissionsAllowed = Permission.requestPermissions(this, Permission.LOCATION_PERMISSIONS, 1234);
        if(permissionsAllowed)
            run();
    }

    private void run() {
        handler = LocationHandler.getInstance();

        handler.init(this, location -> {
            toast("ready");
            gotNewLocation(location);
        });

        handler.startLocationUpdates(1, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                gotNewLocation(locationResult.getLastLocation());
            }
        });

        homeBtn.setOnClickListener(v -> {
            if(!handler.isGPSEnabled()) {
                toast("Turn on you GPS");
                return;
            }
            handler.getLocation(location -> setHomeLocation(location));
        });
    }

    private void gotNewLocation(Location location) {
        if(location == null) return;

        if(homeLocation != null) {
            float distance = homeLocation.distanceTo(location);
            distanceText.setText((distance > 1000) ? distance / 1000 + "km" : distance + "m");

            if(distance > THRESHOLD_DISTANCE)
                toast("You are tresspassing.");
        }

        // UI update
        latitudeText.setText(String.valueOf(location.getLatitude()));
        longitudeText.setText(String.valueOf(location.getLongitude()));
    }

    private void setHomeLocation(Location location) {
        homeLocation = location;
        addressText.setText(handler.getAddressFrom(location));
    }

    private void initViews() {
        latitudeText = findViewById(R.id.latitude);
        longitudeText = findViewById(R.id.longitude);
        addressText = findViewById(R.id.homeAddress);
        distanceText = findViewById(R.id.homeDistance);
        homeBtn = findViewById(R.id.setHomeBtn);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1234 && Permission.accepted(grantResults))
            run();
        else
            toast("this app can't work without location");
    }
}
