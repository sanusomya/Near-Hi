package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {

    Toolbar toolbar1;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar1=(Toolbar) findViewById(R.id.customtoolbar);
        setSupportActionBar(toolbar1);
    }


}