package dev.regucorp.coronaboundary;

import android.location.Location;
import android.os.Bundle;
import android.widget.Chronometer;

import androidx.annotation.Nullable;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import dev.regucorp.coronaboundary.map.MapHandler;
import dev.regucorp.coronaboundary.notifications.NotificationSender;
import dev.regucorp.coronaboundary.position.LocationHandler;
import dev.regucorp.coronaboundary.time.TimeHandler;
import dev.regucorp.coronaboundary.time.TimeListener;

/**
 * Created by Guillaume ROUSSIN on 06/12/20
 */
public class MapsActivity extends BaseActivity implements OnMapReadyCallback, TimeListener {

    private static final int OOB_ID = 1234;

    private Location homeLocation;
    private double maxDistance = 0.02;

    private LocationHandler handler;
    private NotificationSender sender;

    // Views
    private SupportMapFragment mapFragment;
    private Chronometer chronometer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTag("MAP");

        handler = LocationHandler.getInstance();
        sender = NotificationSender.getInstance(this);

        initViews();
        run();
    }

    private void initViews() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        chronometer = findViewById(R.id.map_timer);
    }

    private void run() {
        TimeHandler timer = new TimeHandler(chronometer, this);
        timer.startTimer();

        sender.notify("hello", "world", 123, true);


        homeLocation = (Location) getIntent().getExtras().get("address");
        log(homeLocation.toString());

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        log("Map ready");

        MapHandler map = new MapHandler(this, googleMap);
        map.centerOnLocation(homeLocation, 15f);
        map.addMarker(homeLocation);
        map.drawCircle(homeLocation, maxDistance);

        handler.startLocationUpdates(5, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location l = locationResult.getLastLocation();
                //log("tick");

                if(l.distanceTo(homeLocation) > maxDistance * 1000) {
                    sender.notify("Warning", "you are out of bounds !", OOB_ID, true);
                } else {
                    sender.delete(OOB_ID);
                }
            }
        });
    }

    @Override
    public void onNewSecond() {

    }

    @Override
    public void onNewMinute() {
        log("new minute");
    }

    @Override
    public void onNewHour() {

    }
}
