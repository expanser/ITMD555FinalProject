package com.example.followup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    int count = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();
    }

    public void onLoginClick(View view) {

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

        db.collection("userlist")
                .whereEqualTo("email", email.getText().toString())
                .whereEqualTo("password", password.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 1) {
                                String userId = null;
                                User user = new User();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    userId = document.getId();
                                    user = document.toObject(User.class);
                                }
                                //save to SharedPreferences
                                SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("id", userId);
                                editor.putString("email", user.getEmail());
                                editor.putString("firstName", user.getFirstName());
                                editor.putString("lastName",user.getLastName());
                                editor.commit();

                                Toast.makeText(getApplicationContext(), "Redirecting…", Toast.LENGTH_LONG).show();
                                // go to home page
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        Intent intent= new Intent();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setClass(getApplicationContext(),HomeActivity.class);
                                        startActivity(intent);
                                    }
                                }, 1000);
                            }  else {
                                count--;
                                showWarnMessage();
                            }
                        } else {
                            Log.w("onFailure", "Error getting documents.", task.getException());
                        }
                    }
                });

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
