package com.example.mobileassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Profile_screen extends AppCompatActivity {

    private Button button_profile;
    private Button button_home;
    private Button button_settings;
    private EditText first_name;
    private EditText last_name;
    private EditText date_of_birth;
    private Button button_change_fn;
    private Button button_change_ln;
    private Button button_change_dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        // Getting variables from xml
        button_profile = findViewById(R.id.button_profile);
        button_home = findViewById(R.id.button_home);
        button_settings = findViewById(R.id.button_settings);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        date_of_birth = findViewById(R.id.date_of_birth);
        button_change_fn = findViewById(R.id.button_change_fn);
        button_change_ln = findViewById(R.id.button_change_ln);
        button_change_dob = findViewById(R.id.button_change_dob);

        first_name.setText(Start_screen.getFirst_name());
        last_name.setText(Start_screen.getLast_name());
        date_of_birth.setText(Start_screen.getDate_of_birth());

        // TODO NEED TO IMPLEMENT A CHANGE NAME FUNCTIONALITY
        /*
        // Button to change first name
        button_change_fn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Start_screen.setFirst_name(first_name.getText().toString());
            }
        });

        // Button to change last name
        button_change_ln.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Start_screen.setLast_name(last_name.getText().toString());
            }
        });

        // Button to date of birth
        button_change_dob.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Start_screen.setDate_of_birth(date_of_birth.getText().toString());
            }
        });
        */

        // button to swap to Home screen
        button_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                open_Home_screen(); // opens Home class/screen
            }
        });

        // button to swap to Settings screen
        button_settings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                open_Settings_screen(); // switches to Settings class/screen
            }
        });
    }

    // method for opening Settings screen
    public void open_Settings_screen(){
        Intent intent = new Intent(this, Settings_screen.class);
        startActivity(intent);
    }

    // method for opening Home screen
    public void open_Home_screen(){
        Intent intent = new Intent(this, Home_screen.class);
        startActivity(intent);
    }

    // Just for debugging purposes
    public void makeToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}