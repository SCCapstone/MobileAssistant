package com.example.mobileassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Homescreen extends AppCompatActivity {

    private Button button_profile;
    private Button button_homescreen;
    private Button button_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        // Getting variables from xml
        button_profile = findViewById(R.id.button_profile);
        button_homescreen = findViewById(R.id.button_homescreen);
        button_settings = findViewById(R.id.button_settings);

        // button to swap to Profile screen
        button_profile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                open_Profile_screen();
            }
        });

        // Don't really need to implement since we are already on Home screen
        button_homescreen.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                open_test();
            }
        });

        // button to swap to Settings screen
        button_settings.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                open_Settings_screen();
            }
        });
    }

    // method for openning Profile screen
    public void open_Profile_screen(){
//        Intent intent = new Intent(this, Profile_screen.class);
//        startActivity(intent);
    }

    // method for openning Settings screen
    public void open_Settings_screen(){
//        Intent intent = new Intent(this, Settings_screen.class);
//        startActivity(intent);
    }

    public void open_test(){
        Intent intent = new Intent(this, Start_screen.class);
        startActivity(intent);
    }
}