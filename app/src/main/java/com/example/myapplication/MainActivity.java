package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import com.example.myapplication.fragments.ChatFragment;
import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.fragments.LocationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        //setting the on navigation listner
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavlistner);
        //setting fragment for first time entering the screen
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }

    //creating listners for activating fragments
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavlistner=
            item -> {
                Fragment selectedFragment=null;
                switch (item.getItemId()){
                    case R.id.bottom_home:
                        selectedFragment=new HomeFragment();
                        break;
                    case R.id.bottom_location:
                        selectedFragment=new LocationFragment();
                        break;
                    case R.id.bottom_chat:
                        selectedFragment=new ChatFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                return true;
            };
}