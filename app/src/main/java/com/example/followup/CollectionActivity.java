package com.example.followup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    SharedPreferences sharedPreferences;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = FirebaseFirestore.getInstance();

        getMyNewsCollections();
    }

    public void getMyNewsCollections() {
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("id","");

        db.collection("newsmarklist")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<NewsBookmark> list = new ArrayList<>(task.getResult().toObjects(NewsBookmark.class));
                            getNewsList(list);
                        } else {
                            Log.w("onFailure", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void getNewsList (List<NewsBookmark> bookmarks) {
        List<String> ids = new ArrayList<>();
        for (NewsBookmark item : bookmarks) {
            ids.add(item.getNewsId());
        }
        db.collection("newslist")
                .whereIn("id", ids)
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
                    ListView mListView = findViewById(R.id.news_list);
                    mListView.setAdapter(new NewsAdapter(getApplicationContext(), list));
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
                    //long click to delete
                    mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            new AlertDialog.Builder(CollectionActivity.this).setTitle("Confirm Delete")
                                    .setMessage("Are you sure to delete this news?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.i("YES" , String.valueOf(position));
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.i("NO" , String.valueOf(position));
                                        }
                                    }).show();
                            return true;
                        }
                    });
                } else {
                    Log.w("onFailure", "Error getting documents.", task.getException());
                }
            }
        });
    }
}