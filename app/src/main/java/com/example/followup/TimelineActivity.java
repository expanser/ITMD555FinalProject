package com.example.followup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class TimelineActivity extends AppCompatActivity {

    private FirebaseFirestore db;
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
        //add list
        ListView mListView = findViewById(R.id.timeline_list);
        mListView.setAdapter(new TimelineAdapter(this, new ArrayList<>()));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView arg0, View arg1, int
                    position, long arg3) {
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                i.putExtra("position", position);
                startActivity(i);
            }
        });

        //add save to collection button listener
        FloatingActionButton SaveButton = findViewById(R.id.SaveButton);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Add To Collections Success!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getTimelineInfo () {
        //todo
        DocumentReference docRef = db.collection("eventlist").document(eventId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i("eventItem--", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.i("eventItem--", "No such document" + eventId);
                    }
                } else {
                    Log.i("eventItem--", "get failed with ", task.getException());
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