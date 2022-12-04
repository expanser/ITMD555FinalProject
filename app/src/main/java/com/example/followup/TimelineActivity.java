package com.example.followup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class TimelineActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    SharedPreferences sharedPreferences;
    String eventId;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        eventId = i.getStringExtra("id");

        getTimelineList();
        getTimelineInfo();

        //add save to collection button listener
        FloatingActionButton SaveButton = findViewById(R.id.SaveButton);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                String userId = sharedPreferences.getString("id","");
                //check if it has been added before
                db.collection("eventmarklist")
                        .whereEqualTo("userId", userId)
                        .whereEqualTo("eventId", eventId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() > 0) {
                                        Toast.makeText(getApplicationContext(), "This event has been added already",
                                                Toast.LENGTH_LONG).show();
                                    }  else {
                                        addToEventBookmark(new EventBookmark( userId, eventId ));
                                    }
                                } else {
                                    Log.w("onFailure", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });
    }

    public void addToEventBookmark (EventBookmark eventBookmark) {
        db.collection("eventmarklist").add(eventBookmark).
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

    public void getTimelineInfo () {
        db.collection("eventlist")
                .whereEqualTo("id", eventId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            EventItem eventItem = task.getResult().toObjects(EventItem.class).get(0);

                            TextView titleView = findViewById(R.id.titleView);
                            titleView.setText(eventItem.getTitle());
                            Picasso.get().load(eventItem.getThumbnail()).into((ImageView) findViewById(R.id.eventThumb));
                        } else {
                            Log.w("onFailure", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void getTimelineList () {
        db.collection("newslist")
                .whereEqualTo("eventId", eventId)
                .orderBy("releaseTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<NewsItem> list = new ArrayList<>();
                    List<String> ids = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ids.add(document.getId());
                        list.add(document.toObject(NewsItem.class));
                    }
                    ListView mListView = findViewById(R.id.timeline_list);
                    mListView.setAdapter(new TimelineAdapter(getApplicationContext(), list));
                    //click to go to article
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView arg0, View arg1, int
                                position,long arg3) {
                            Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                            i.putExtra("id", ids.get(position));
                            i.putExtra("link", list.get(position).getLink());
                            i.putExtra("eventId", list.get(position).getEventId());
                            startActivity(i);
                        }
                    });
                } else {
                    Log.w("onFailure", "Error getting documents.", task.getException());
                }
            }
        });
    }
}