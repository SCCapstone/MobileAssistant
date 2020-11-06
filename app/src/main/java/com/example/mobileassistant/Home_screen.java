package com.example.mobileassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home_screen extends AppCompatActivity {

    private Button button_profile;
    private Button button_home;
    private Button button_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Getting variables from xml
        button_profile = findViewById(R.id.button_profile);
        button_home = findViewById(R.id.button_home);
        button_settings = findViewById(R.id.button_settings);

        // button to swap to Profile screen
        button_profile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                open_Profile_screen(); // opens Profile class/screen
            }
        });

        // button to swap to Settings screen
        button_settings.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                open_Settings_screen(); // opens Settings class/screen
            }
        });

    }

    // method for opening Profile screen
    public void open_Profile_screen(){
        Intent intent = new Intent(this, Profile_screen.class);
        startActivity(intent);
    }

    // method for opening Settings screen
    public void open_Settings_screen(){
        Intent intent = new Intent(this, Settings_screen.class);
        startActivity(intent);
    }

}