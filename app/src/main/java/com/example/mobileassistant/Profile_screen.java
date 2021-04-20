package com.example.mobileassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.DatePicker;
import android.widget.TextView;
import android.app.DatePickerDialog;

import java.util.Calendar;
import java.util.Random;

public class Profile_screen extends AppCompatActivity {

    // CONSTANTS for Account Preferences
    private static final String ACCOUNT_PREFERENCES = "ACCOUNT";
    public static final String  ACCOUNT_PROFILE_PHOTO = "PROFILE_PHOTO";
    private static final String ACCOUNT_FIRST_NAME = "FIRST_NAME";
    private static final String ACCOUNT_LAST_NAME = "LAST_NAME";
    private static final String ACCOUNT_DOB = "DOB";
    private DatePickerDialog.OnDateSetListener adateSetListener;

    //CONSTANTS for changing photos
    private static final int NUM_PHOTOS = 5 ;
    int[] images;

    private ImageButton change_photo;
    private Button button_home;
    private Button button_settings;
    private EditText first_name;
    private EditText last_name;
    private TextView date_of_birth;
    private Button button_change_fn;
    private Button button_change_ln;
    private Button button_change_dob;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        // Getting variables from xml
        change_photo = findViewById(R.id.imageButton);
        button_home = findViewById(R.id.button_home);
        button_settings = findViewById(R.id.button_settings);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        date_of_birth = (TextView)findViewById(R.id.date_of_birth);
        button_change_fn = findViewById(R.id.button_change_fn);
        button_change_ln = findViewById(R.id.button_change_ln);
        button_change_dob = findViewById(R.id.button_change_dob);

        // Use ACCOUNT_PREFERENCES to display user information
        sharedPreferences = getSharedPreferences(ACCOUNT_PREFERENCES, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.contains(ACCOUNT_PROFILE_PHOTO)) {
            int accountImageRId = sharedPreferences.getInt(ACCOUNT_PROFILE_PHOTO, -1);
            change_photo.setImageResource(accountImageRId);
        }

        String accountFirstName = sharedPreferences.getString(ACCOUNT_FIRST_NAME, null);
        first_name.setText(accountFirstName);
        first_name.setEnabled(false);

        String accountLastName = sharedPreferences.getString(ACCOUNT_LAST_NAME, null);
        last_name.setText(accountLastName);
        last_name.setEnabled(false);

        String accountDOB = sharedPreferences.getString(ACCOUNT_DOB, null);
        date_of_birth.setText(accountDOB);
        date_of_birth.setEnabled(false);

        // ImageButton to change profile photo
        change_photo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                changeIcon(view);
            }
        });

        // Button to change first name
        button_change_fn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(first_name.isEnabled()==false)
                {
                    first_name.setEnabled(true);
                }
                else if(first_name.isEnabled()==true)
                {
                    first_name.setEnabled(false);
                }
            }
        });

        // Button to change last name
        button_change_ln.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(last_name.isEnabled()==false)
                {
                    last_name.setEnabled(true);
                }
                else if(last_name.isEnabled()==true)
                {
                    last_name.setEnabled(false);
                }
            }
        });

        // Button to date of birth
        button_change_dob.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(date_of_birth.isEnabled()==false)
                {
                    date_of_birth.setEnabled(true);
                }
                else if(date_of_birth.isEnabled()==true)
                {
                    date_of_birth.setEnabled(false);
                }
            }
        });

        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DATE);

                // default to current date
                DatePickerDialog dp = new DatePickerDialog(Profile_screen.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, adateSetListener,
                        year, month, day);
                dp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dp.show();
            }
        });
        // allow user to pick a date
        adateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;
                String date = month + "/" +dayOfMonth +"/" + year;
                date_of_birth.setText(date);
            }
        };

        // button to swap to Home screen
        button_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                first_name = findViewById(R.id.first_name);
                editor.putString(ACCOUNT_FIRST_NAME, first_name.getText().toString());
                last_name = findViewById(R.id.last_name);
                editor.putString(ACCOUNT_LAST_NAME, last_name.getText().toString());
                date_of_birth = (TextView)findViewById(R.id.date_of_birth);
                editor.putString(ACCOUNT_DOB, date_of_birth.getText().toString());
                editor.apply();
                open_Home_screen(); // opens Home class/screen
            }
        });

        // button to swap to Settings screen
        button_settings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                first_name = findViewById(R.id.first_name);
                editor.putString(ACCOUNT_FIRST_NAME, first_name.getText().toString());
                last_name = findViewById(R.id.last_name);
                editor.putString(ACCOUNT_LAST_NAME, last_name.getText().toString());
                date_of_birth = (TextView)findViewById(R.id.date_of_birth);
                editor.putString(ACCOUNT_DOB, date_of_birth.getText().toString());
                editor.apply();
                open_Settings_screen(); // switches to Settings class/screen
            }
        });
    }

    //change user icon
    public void changeIcon(View view) {
        images = new int[]{R.drawable.user_icon_0, R.drawable.user_icon_1, R.drawable.user_icon_2, R.drawable.user_icon_3, R.drawable.user_icon_4};
        Random random = new Random();
        int rNum = random.nextInt(NUM_PHOTOS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ACCOUNT_PROFILE_PHOTO, images[rNum]);
        editor.apply();
        change_photo.setImageDrawable(getResources().getDrawable(images[rNum]));
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