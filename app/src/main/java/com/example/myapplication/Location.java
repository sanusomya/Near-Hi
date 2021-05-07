package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import static androidx.core.location.LocationManagerCompat.isLocationEnabled;

public class Location extends AppCompatActivity {
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    TextView lat;
    TextView lon;
    TextView alt;
    TextView acc;
    TextView speed;
    TextView addr;
    TextView updates;
    TextView sensors;

    SwitchCompat sw_location_update, sw_gps;


    LocationRequest locationRequest;

    LocationCallback locationCallback;
    // API for location services
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // finding toolbar and setting it as the default
        androidx.appcompat.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //hooking all the other views for side drawer
        NavigationView navigationView=findViewById(R.id.nav_view);
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);

        //activating the drawer button
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //hooking the bottom navigation
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav);
        //setting the default item
        bottomNavigationView.setSelectedItemId(R.id.bottom_location);
        //setting the on navigation listner
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.bottom_chat:
                        startActivity(new Intent(getApplicationContext(),Chat.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.bottom_location:
                        break;
                }
                return true;
            }
        });

        //hooking the location parameters
        lat = findViewById(R.id.tv_lat);
        lon = findViewById(R.id.tv_lon);
        alt = findViewById(R.id.tv_altitude);
        acc = findViewById(R.id.tv_accuracy);
        speed = findViewById(R.id.tv_speed);
        addr = findViewById(R.id.tv_address);
        updates = findViewById(R.id.tv_updates);
        sensors = findViewById(R.id.tv_sensor);

        sw_gps = findViewById(R.id.sw_gps);
        sw_location_update = findViewById(R.id.sw_locationsupdates);

        //set properties of location request

        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000 * 30);
        locationRequest.setFastestInterval(1000 * 5);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

//even is triggered whenever update interval is made
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                android.location.Location location = locationResult.getLastLocation();
                updateUIvalues(location);

            }
        };
        //setting listener for switch

        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_gps.isChecked()) { //location permission granted
                    //use most accurate
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    sensors.setText("Using GPS sensors");
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    sensors.setText("Using cellTower/WiFi");
                }
            }
        });

        sw_location_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sw_location_update.isChecked()) {
                    //turn on interval tracking
                    startLocationupdates();

                } else {
                    //turn off tracking
                    stopLocationupdates();
                }
            }
        });


        updateGPS();
    }
    private void startLocationupdates() {
        updates.setText("Location is being updated");
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        updateGPS();


    }

    private void stopLocationupdates() {
        updates.setText(("Location updates are stopped"));
        lat.setText("Not tracking");
        alt.setText("Not tracking");
        lon.setText("Not tracking");
        speed.setText("Not tracking");
        acc.setText("Not tracking");
        addr.setText("Not tracking");
        sensors.setText("Not tracking");

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSIONS_FINE_LOCATION:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
                    updateGPS();
                else
                {
                    Toast.makeText(this, "This app needs permission to granted in order to work properly", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void updateGPS()
    {
        //get permission from user to track gps
        //update all textview according to their properties

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(Location.this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            //user provided the permission
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                @Override
                public void onSuccess(android.location.Location location) {
                    //got the permission now assign the values
                    updateUIvalues(location);

                }
            });

        }
        else
        {
            //no permission granted
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    private void updateUIvalues(android.location.Location location) {
        LocationManager locationManager=(LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);

        if (isLocationEnabled(locationManager) && location != null) {

            //update textviews with location
            lat.setText(String.valueOf(location.getLatitude()));
            lon.setText(String.valueOf(location.getLongitude()));
            acc.setText(String.valueOf(location.getAccuracy()));
            if(location.hasAltitude())
            {
                alt.setText(String.valueOf(location.getAltitude()));
            }
            else
            {
                alt.setText("Not available");
            }
            if(location.hasSpeed())
            {
                speed.setText(String.valueOf(location.getSpeed()));
            }
            else
            {
                alt.setText("Not available");
            }
            Geocoder geocoder=new Geocoder(Location.this);

            try
            {
                List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                addr.setText(addresses.get(0).getAddressLine(0));
            }
            catch(Exception e)
            {
                addr.setText("Unable to get street address");
            }
        }
    }
}