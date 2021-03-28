package com.example.mobileassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Profile_screen extends AppCompatActivity {

    // CONSTANTS for Account Preferences
    private static final String ACCOUNT_PREFERENCES = "ACCOUNT";
    private static final String ACCOUNT_FIRST_NAME = "FIRST_NAME";
    private static final String ACCOUNT_LAST_NAME = "LAST_NAME";
    private static final String ACCOUNT_DOB = "DOB";

    private Button button_home;
    private Button button_settings;
    private EditText first_name;
    private EditText last_name;
    private EditText date_of_birth;
    private Button button_change_fn;
    private Button button_change_ln;
    private Button button_change_dob;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        // Getting variables from xml
        button_home = findViewById(R.id.button_home);
        button_settings = findViewById(R.id.button_settings);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        date_of_birth = findViewById(R.id.date_of_birth);
        button_change_fn = findViewById(R.id.button_change_fn);
        button_change_ln = findViewById(R.id.button_change_ln);
        button_change_dob = findViewById(R.id.button_change_dob);

        // Use ACCOUNT_PREFERENCES to display user information
        sharedPreferences = getSharedPreferences(ACCOUNT_PREFERENCES, MODE_PRIVATE);

        String accountFirstName = sharedPreferences.getString(ACCOUNT_FIRST_NAME, null);
        first_name.setText(accountFirstName);

        String accountLastName = sharedPreferences.getString(ACCOUNT_LAST_NAME, null);
        last_name.setText(accountLastName);

        String accountDOB = sharedPreferences.getString(ACCOUNT_DOB, null);
        date_of_birth.setText(accountDOB);

        final SharedPreferences.Editor editor = sharedPreferences.edit();

        // Button to change first name
        button_change_fn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                first_name = findViewById(R.id.first_name);
                editor.putString(ACCOUNT_FIRST_NAME, first_name.getText().toString());
                editor.apply();
            }
        });

        // Button to change last name
        button_change_ln.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                last_name = findViewById(R.id.last_name);
                editor.putString(ACCOUNT_LAST_NAME, last_name.getText().toString());
                editor.apply();
            }
        });

        // Button to date of birth
        button_change_dob.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                date_of_birth = findViewById(R.id.date_of_birth);
                editor.putString(ACCOUNT_DOB, date_of_birth.getText().toString());
                editor.apply();
            }
        });


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