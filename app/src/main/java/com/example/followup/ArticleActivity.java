package com.example.followup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ArticleActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    SharedPreferences sharedPreferences;
    String newsId;
    String link;
    String eventId;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        newsId = i.getStringExtra("id");
        link = i.getStringExtra("link");
        eventId = i.getStringExtra("eventId");
        WebView mWebview = findViewById(R.id.webView);
        mWebview.loadUrl(link);
    }

    public void onMoreClick(View view) {
        if (eventId != null) {
            Intent i = new Intent(getApplicationContext(), TimelineActivity.class);
            i.putExtra("id", eventId);
            startActivity(i);
        } else {
            Toast.makeText(this, "This news is not related to an event yet!", Toast.LENGTH_LONG).show();
        }
    }

    public void onAddClick(View view) {
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("id","");
        //check if it has been added before
        db.collection("newsmarklist")
                .whereEqualTo("userId", userId)
                .whereEqualTo("newsId", newsId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                Toast.makeText(getApplicationContext(), "This news has been added already",
                                        Toast.LENGTH_LONG).show();
                            }  else {
                                addToNewsBookmark(new NewsBookmark( userId, newsId ));
                            }
                        } else {
                            Log.w("onFailure", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void addToNewsBookmark (NewsBookmark newsBookmark) {
        db.collection("newsmarklist").add(newsBookmark).
                addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("onSuccess", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "Add To Collections Success!", Toast.LENGTH_LONG).show();
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