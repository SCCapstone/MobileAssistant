package com.example.mobileassistant;

import androidx.appcompat.app.AppCompatActivity;
/**
 * We need to import a local database
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText ;

public class Start_screen extends AppCompatActivity {

    private EditText first_name;
    private EditText last_name;
    private EditText date_of_birth;
    private Button button_confirm;

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
                // We just store the first name, last name, and date of birth in local DB
                String f_name = first_name.getText().toString();
                String l_name = last_name.getText().toString();
                String dob = date_of_birth.getText().toString();

                // Then we go to the homescreen.
                open_Homescreen();
            }
        });
    }

    // This method is used for swapping to the homescreen class and xml
    public void open_Homescreen(){
        Intent intent = new Intent(this, Homescreen.class); // Setting intent
        startActivity(intent); // open the activity
    }
}