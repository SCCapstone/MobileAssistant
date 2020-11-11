package com.example.mobileassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Settings_screen extends AppCompatActivity {

    private Button button_profile;
    private Button button_home;
    private Button button_settings;
    private Button button_light_mode;
    private Button button_dark_mode;
    SharedPreferences sharedPreferences = null; // For color theme

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        // Getting variables from xml
        button_profile = findViewById(R.id.button_profile);
        button_home = findViewById(R.id.button_home);
        button_settings = findViewById(R.id.button_settings);
        button_light_mode = findViewById(R.id.button_light_mode);
        button_dark_mode = findViewById(R.id.button_dark_mode);


        // Keeps track of Light/Night Mode, saves the state of Light/Dark mode when reopened
        sharedPreferences = getSharedPreferences("night", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final Boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        // Light Mode Button
        button_light_mode.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (isDarkModeOn) {
                    // if dark mode is on it
                    // will turn it off
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    // it will set isDarkModeOn
                    // boolean to false
                    editor.putBoolean("isDarkModeOn", false);
                    editor.apply();
                }
            }
        });

        // Dark Mode Button
        button_dark_mode.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (!isDarkModeOn) {
                    // if dark mode is off it
                    // will turn it on
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    // it will set isDarkModeOn
                    // boolean to true
                    editor.putBoolean("isDarkModeOn", true);
                    editor.apply();
                }
            }
        });

        // button to swap to Profile screen
        button_profile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                open_Profile_screen(); // opens Profile class/screen
            }
        });

        // button to swap to Home screen
        button_home.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                open_Home_screen(); // opens Home class/screen
            }
        });

    }

    // method for opening Profile screen
    public void open_Profile_screen(){
        Intent intent = new Intent(this, Profile_screen.class);
        startActivity(intent);
    }

    // method for opening Home screen
    public void open_Home_screen(){
        Intent intent = new Intent(this, Home_screen.class);
        startActivity(intent);
    }
}