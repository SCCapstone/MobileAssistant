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
import android.widget.Toast;

public class Start_screen extends AppCompatActivity {

    // static if you want to use outside of class
    private static EditText first_name;
    private static EditText last_name;
    private static EditText date_of_birth;
    private Button button_confirm;
    SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

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
                && !date_of_birth.getText().toString().equals(""))
                    open_Homescreen();
                else{
                    makeToast("Please Enter All Information");
                }
            }
        });

        // Keeps track of Light/Night Mode, saves the state of Light/Dark mode when reopened
        sharedPreferences = getSharedPreferences("night", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final Boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        // Saves the current state when user exits app for Light/Dark Mode
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
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

    public static void setFirst_name(String s) {
        first_name.setText(s);
    }

    public static String getLast_name() {
        if (last_name.getText().toString().equals("")){
            return "No Last Name";
        }
        else{
            return last_name.getText().toString();
        }
    }

    public static void setLast_name(String s) {
        last_name.setText(s);
    }

    public static String getDate_of_birth() {
        if (date_of_birth.getText().toString().equals("")){
            return "No Date of Birth";
        }
        else{
            return date_of_birth.getText().toString();
        }
    }

    public static void setDate_of_birth(String s) {
        date_of_birth.setText(s);
    }

    // Just for debugging
    public void makeToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}