package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class Chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
        bottomNavigationView.setSelectedItemId(R.id.bottom_chat);
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
                        break;
                    case R.id.bottom_location:
                        startActivity(new Intent(getApplicationContext(),Location.class));
                        overridePendingTransition(0,0);
                        break;
                }
                return true;
            }
        });
    }
}