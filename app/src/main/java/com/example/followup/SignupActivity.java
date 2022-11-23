package com.example.followup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void onSignupClick(View view) {

        EditText email, password, firstName, lastName;
        email = findViewById(R.id.textUserEmail);
        password = findViewById(R.id.textPassword);
        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);

        if (firstName.getText().length() == 0 || lastName.getText().length() == 0) {
            Toast.makeText(this, "Please enter a valid first name and last name",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (email.getText().length() == 0 || password.getText().length() == 0) {
            Toast.makeText(this, "Please enter a valid E-mail and password",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (true) {
            //go back to login page
            Toast.makeText(this, "sign up success!",
                    Toast.LENGTH_LONG).show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }, 1000);
        }

    }

}
