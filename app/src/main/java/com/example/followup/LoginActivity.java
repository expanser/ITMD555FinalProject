package com.example.followup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    int count = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginClick(View view) {

        String correctEmail = "nathan", correctPassword = "123456";
        EditText email, password;
        email = findViewById(R.id.textUserEmail);
        password = findViewById(R.id.textPassword);

        //too many tries
        if (count == 0) {
            Toast.makeText(this, "reach maximum tries times",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (email.getText().length() == 0 || password.getText().length() == 0) {
            Toast.makeText(this, "Please enter a valid E-mail and password",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (email.getText().toString().equals(correctEmail) && password.getText().toString().equals(correctPassword)) {
            Toast.makeText(this, "Redirecting…", Toast.LENGTH_LONG).show();
            // go to home page and not allowed to come back
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent= new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                }
            }, 1000);
        } else {
            count--;
            showWarnMessage();
        }

    }

    public void showWarnMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Invalid E-mail or password");
        builder.setMessage("Tries left => " + count);
        builder.show();
    }

    public void onSignupClick(View view) {
        startActivity(new Intent(getApplicationContext(), SignupActivity.class));
    }

}
