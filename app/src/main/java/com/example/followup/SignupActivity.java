package com.example.followup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Timer;
import java.util.TimerTask;

public class SignupActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = FirebaseFirestore.getInstance();
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

        //check if E-mail has been used
        db.collection("userlist")
                .whereEqualTo("email", email.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                Toast.makeText(getApplicationContext(), "This E-mail has been used! Please input a new one",
                                Toast.LENGTH_LONG).show();
                            }  else {
                                User user = new User(email.getText().toString(), password.getText().toString(), firstName.getText().toString(), lastName.getText().toString());
                                doSignup(user);
                            }
                        } else {
                            Log.w("onFailure", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void doSignup(User user) {
        db.collection("userlist").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("onSuccess", "DocumentSnapshot added with ID: " + documentReference.getId());
                //go back to login page
                Toast.makeText(getApplicationContext(), "sign up success!", Toast.LENGTH_LONG).show();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent= new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                }, 1000);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("onFailure", "Error adding document", e);
            }
        });
    }

}
