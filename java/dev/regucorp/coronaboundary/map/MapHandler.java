package dev.regucorp.coronaboundary.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Guillaume ROUSSIN on 13/10/20
 */
public class MapHandler {

    private GoogleMap googleMap;

    public MapHandler(AppCompatActivity act, GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Init map
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(act.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(act.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    public void smoothCenterOnLocation(Location pos, float zoom) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toLatLng(pos), zoom));
    }

    public void centerOnLocation(Location pos, float zoom) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toLatLng(pos), zoom));
    }

    public void addMarker(Location pos) {
        MarkerOptions markerOptions = new MarkerOptions().position(toLatLng(pos));
        googleMap.addMarker(markerOptions);
    }

    public void drawCircle(Location center, double radius) {
        CircleOptions circleOptions = new CircleOptions()
                .center(toLatLng(center))
                .radius(radius * 1000)
                .fillColor(Color.argb(64, 0, 255, 255))
                .strokeColor(Color.rgb(0, 255, 255));
        googleMap.addCircle(circleOptions);
    }

    private LatLng toLatLng(Location l) {
        return new LatLng(l.getLatitude(), l.getLongitude());
    }
}
