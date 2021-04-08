package com.example.mobileassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class Profile_screen extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    // CONSTANTS for Account Preferences
    private static final String ACCOUNT_PREFERENCES = "ACCOUNT";
    public static final String  ACCOUNT_PROFILE_PHOTO = "PROFILE_PHOTO";
    public static final int GET_FROM_GALLERY = 0;
    private static final String ACCOUNT_FIRST_NAME = "FIRST_NAME";
    private static final String ACCOUNT_LAST_NAME = "LAST_NAME";
    private static final String ACCOUNT_DOB = "DOB";

    //CONSTANTS for changing photos
    private static final int NUM_PHOTOS = 5 ;
    int[] images;

    private ImageButton change_photo;
    private Button button_home;
    private Button button_settings;
    private EditText first_name;
    private EditText last_name;
    private EditText date_of_birth;
    private Button button_change_fn;
    private Button button_change_ln;
    private Button button_change_dob;


    SharedPreferences sharedPreferences;

    //used for the onClick for the image button
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
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
        date_of_birth = findViewById(R.id.date_of_birth);
        button_change_fn = findViewById(R.id.button_change_fn);
        button_change_ln = findViewById(R.id.button_change_ln);
        button_change_dob = findViewById(R.id.button_change_dob);

        change_photo.setImageResource(R.drawable.user_icon);
        // Use ACCOUNT_PREFERENCES to display user information

        sharedPreferences = getSharedPreferences(ACCOUNT_PREFERENCES, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        String accountFirstName = sharedPreferences.getString(ACCOUNT_FIRST_NAME, null);
        first_name.setText(accountFirstName);
        first_name.setEnabled(false);

        String accountProfilePhoto = sharedPreferences.getString(ACCOUNT_PROFILE_PHOTO,null);
        if(accountProfilePhoto != null){
            change_photo.setImageBitmap(BitmapFactory.decodeFile(accountProfilePhoto));
        }



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


        // button to swap to Home screen
        button_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                first_name = findViewById(R.id.first_name);
                editor.putString(ACCOUNT_FIRST_NAME, first_name.getText().toString());
                last_name = findViewById(R.id.last_name);
                editor.putString(ACCOUNT_LAST_NAME, last_name.getText().toString());
                date_of_birth = findViewById(R.id.date_of_birth);
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
                date_of_birth = findViewById(R.id.date_of_birth);
                editor.putString(ACCOUNT_DOB, date_of_birth.getText().toString());
                editor.apply();
                open_Settings_screen(); // switches to Settings class/screen
            }
        });
    }


    //change user icon
    public void changeIcon(View view) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent,GET_FROM_GALLERY);
            verifyStoragePermissions(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (selectedImage != null) {
                Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    ImageButton imageButton = findViewById(R.id.imageButton);
                    imageButton.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ACCOUNT_PROFILE_PHOTO, picturePath);
                    editor.apply();
                    cursor.close();
                }
            }
        }
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