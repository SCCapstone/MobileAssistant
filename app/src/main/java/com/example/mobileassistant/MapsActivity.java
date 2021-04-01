package com.example.mobileassistant;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Int tag for flagging the permissions when requested
    private static final int TAG_CODE_PERMISSION_LOCATION = 123;

    // Key value for EXTRA_MESSAGE string sent from intent
    private static final String EXTRA_MESSAGE = "com.example.mobileassistant.MESSAGE";

    // Variables used for setting up the map and getting the current location
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    // Variables used for searching for a place's location
    private boolean placeFlag = false;
    private String placeName = "";

    // Creates the map and checks if the user wants a place or local traffic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enables a back button that goes to the home screen
        getSupportActionBar().setHomeButtonEnabled(true);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Used for extra strings input through the Home_screen putExtras() method
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // Sets the flag so that the location setter can get the city
        if (bundle != null) {
            placeFlag = true;
            placeName = bundle.getString(EXTRA_MESSAGE);
        }
    }

    // Requests the location when the map is ready
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
            // Enables traffic layer on Google Maps
            mMap.setTrafficEnabled(true);

            // Requests user permissions and sets
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            }

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        updateMap(location);
                    }
                }
            });
        } else {
        ActivityCompat.requestPermissions(this, new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION },
                TAG_CODE_PERMISSION_LOCATION);
    }
    }

    // Called when the permission result is granted/denied
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        updateMap(location);
                    }
                }
            });
        } else {
            Toast.makeText(this, "Location permissions denied", Toast.LENGTH_SHORT).show();
        }
    }

    // Updates the map with the current/last location and zooms in on the point
    public void updateMap(Location location) {
        if (placeFlag == true) {
            getPlaceCoordinates(placeName);
        }
        else {
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            // Animated zoom in on the current location (set zoom level of 12)
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }
    }

    // Gets the coordinates of a place based on its String name
    public void getPlaceCoordinates(String place) {
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocationName(place, 1);
            if (addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                LatLng placeLocation = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
                // Animated zoom in on the current location (set zoom level of 12)
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}