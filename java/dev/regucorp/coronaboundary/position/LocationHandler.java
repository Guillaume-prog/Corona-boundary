package dev.regucorp.coronaboundary.position;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by Guillaume ROUSSIN on 12/08/20
 */
public class LocationHandler {

    public interface GPSListener {
        void onStateChanged(boolean state);
    }

    // Static instance
    private static LocationHandler instance;

    // Variables
    public static final String TAG = "LocationHandler";
    private AppCompatActivity act;

    // Google API location manager
    private FusedLocationProviderClient client;
    private Geocoder geocoder;

    private Location lastLocation;
    private boolean isGpsEnabled;

    // Location requests Callback
    private LocationCallback locationCallback;
    private OnSuccessListener<Location> rebootCallback;
    private GPSListener gpsListener;


    public static LocationHandler getInstance() {
        Log.d(TAG, "" + instance);
        if (instance == null) instance = new LocationHandler();
        return instance;
    }


    public void init(AppCompatActivity act, OnSuccessListener<Location> readyListener) {
        this.act = act;

        client = LocationServices.getFusedLocationProviderClient(act);
        geocoder = new Geocoder(act.getApplicationContext());

        BroadcastReceiver gpsListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().matches("android.location.PROVIDERS_CHANGED"))
                    checkLocationEnabled(context);
            }
        };
        act.registerReceiver(gpsListener, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

        checkLocationEnabled(act.getApplicationContext());
        reboot(readyListener);
    }

    public void reboot(OnSuccessListener<Location> callback) {
        LocationCallback rebootListener = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location l = locationResult.getLastLocation();
                if(l != null) {
                    client.removeLocationUpdates(this);
                    lastLocation = l;
                    callback.onSuccess(l);
                }
            }
        };

        startLocationUpdates(0.1, rebootListener);
    }

    public void setRebootCallback(OnSuccessListener<Location> callback) {
        rebootCallback = callback;
    }


    private void checkLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean state = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(state != isGpsEnabled) {
            if(gpsListener != null)
                gpsListener.onStateChanged(state);

            if(state && rebootCallback != null)
                reboot(rebootCallback);
        }

        isGpsEnabled = state;
    }

    public void setGpsListener(GPSListener listener) {
        gpsListener = listener;
    }

    public boolean isGPSEnabled() {
        return isGpsEnabled;
    }


    public void getLocation(final OnSuccessListener<Location> listener) {
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        client.getLastLocation().addOnSuccessListener(act, location -> {
            Log.d(TAG, "Location: " + location);
            if(location == null)
                listener.onSuccess(lastLocation);
            else {
                Log.d(TAG, "Get location - got new location");
                lastLocation = location;
                listener.onSuccess(location);
            }
        });
    }


    public void startLocationUpdates(double interval, LocationCallback listener) {
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval((long) interval * 1000);
        locationRequest.setFastestInterval((long) interval * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        client.requestLocationUpdates(locationRequest, listener, Looper.getMainLooper());
        locationCallback = listener;
    }

    public void stopLocationUpdates() {
        client.removeLocationUpdates(locationCallback);
        locationCallback = null;
    }


    public String getAddressFrom(Location l) {
        try {
            List<Address> addresses = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
            return addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Location getLocationFrom(String address) {
        try {
            List<Address> locations = geocoder.getFromLocationName(address, 1);
            Location location = new Location( (lastLocation != null) ? lastLocation.getProvider() : "dummy" );
            location.setLatitude(locations.get(0).getLatitude());
            location.setLongitude(locations.get(0).getLongitude());
            return location;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
