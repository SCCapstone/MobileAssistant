package com.example.mobileassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
/**
 * We need to import a local database
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText ;
import android.widget.TextView;
import android.widget.Toast;

public class Start_screen extends AppCompatActivity {

    // CONSTANTS for Account Preferences
    private static final String ACCOUNT_PREFERENCES = "ACCOUNT";
    private static final String ACCOUNT_FIRST_NAME = "FIRST_NAME";
    private static final String ACCOUNT_LAST_NAME = "LAST_NAME";
    private static final String ACCOUNT_DOB = "DOB";

    // CONSTANTS for night mode
    private static final String NIGHT_PREFERENCES = "NIGHT";
    private static final String DARK_MODE = "isDarkModeOn";

    // static if you want to use outside of class
    private static EditText first_name;
    private static EditText last_name;
    private static EditText date_of_birth;
    private Button button_confirm;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        // Keeps track of Light/Night Mode, saves the state of Light/Dark mode when reopened
        sharedPreferences = getSharedPreferences(NIGHT_PREFERENCES, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final Boolean isDarkModeOn = sharedPreferences.getBoolean(DARK_MODE, false);

        // Saves the current state when user exits app for Light/Dark Mode
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Check if user account is already in
        sharedPreferences = getSharedPreferences(ACCOUNT_PREFERENCES, MODE_PRIVATE);
        String accountFirstName = sharedPreferences.getString(ACCOUNT_FIRST_NAME, null);
        String accountLastName = sharedPreferences.getString(ACCOUNT_LAST_NAME, null);
        String accountDOB = sharedPreferences.getString(ACCOUNT_DOB, null);
        // Start with homescreen if there is already an account
        if (accountFirstName != null && accountLastName != null && accountDOB != null)
            open_Homescreen();


        // Getting variables from xml
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        date_of_birth = findViewById(R.id.date_of_birth);
        button_confirm = (Button)findViewById(R.id.button_confirm);

        // Action to take when the "Confirm" button is pressed
        button_confirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                // Check if user entered first name, last name, and date of birth
                if (!first_name.getText().toString().equals("")
                && !last_name.getText().toString().equals("")
                && !date_of_birth.getText().toString().equals("")) {
                    // keep the data for account
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ACCOUNT_FIRST_NAME, first_name.getText().toString());
                    editor.putString(ACCOUNT_LAST_NAME, last_name.getText().toString());
                    editor.putString(ACCOUNT_DOB, date_of_birth.getText().toString());
                    editor.commit(); // save

                    open_Homescreen();
                }
                else{
                    makeToast("Please Enter All Information");
                }
            }
        });
    }

    // This method is used for swapping to the homescreen class and xml
    public void open_Homescreen(){
        Intent intent = new Intent(this, Home_screen.class); // Setting intent
        startActivity(intent); // open the activity
    }

    // Getters and Setters for accessing variables outside of this activity
    public static String getFirst_name() {
        if (first_name.getText().toString().equals("")){
            return "No First Name";
        }
        else{
            return first_name.getText().toString();
        }
    }

    public static String getLast_name() {
        if (last_name.getText().toString().equals("")){
            return "No Last Name";
        }
        else{
            return last_name.getText().toString();
        }
    }

    public static String getDate_of_birth() {
        if (date_of_birth.getText().toString().equals("")){
            return "No Date of Birth";
        }
        else{
            return date_of_birth.getText().toString();
        }
    }

    // Just for debugging
    public void makeToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}