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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
                            List<NewsBookmark> list = new ArrayList<>();
                            List<String> ids = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ids.add(document.getId());
                                list.add(document.toObject(NewsBookmark.class));
                            }
                            getNewsList(list, ids);
                        } else {
                            Log.w("onFailure", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void getNewsList (List<NewsBookmark> bookmarks, List<String> bookmarkIds) {
        List<String> newsIds = new ArrayList<>();
        for (NewsBookmark item : bookmarks) {
            newsIds.add(item.getNewsId());
        }
        db.collection("newslist")
                .whereIn("id", newsIds)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<NewsItem> newsList = new ArrayList<>();
                    List<String> newsIds = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        newsIds.add(document.getId());
                        newsList.add(document.toObject(NewsItem.class));
                    }
                    ListView mListView = findViewById(R.id.news_list);
                    NewsAdapter newsAdapter = new NewsAdapter(getApplicationContext(), newsList);
                    mListView.setAdapter(newsAdapter);
                    //click to go to article
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView arg0, View arg1, int
                                position,long arg3) {
                            Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                            i.putExtra("id", newsIds.get(position));
                            i.putExtra("link", newsList.get(position).getLink());
                            i.putExtra("eventId", newsList.get(position).getEventId());
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
                                            //find id position in bookmarkIds
                                            String deleteId = "";
                                            for (int i = 0; i < bookmarks.size() ;i++) {
                                                if (bookmarks.get(i).getNewsId().equals(newsList.get(position).getId())) {
                                                    deleteId = bookmarkIds.get(i);
                                                }
                                            }
                                            db.collection("newsmarklist").document(deleteId)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("delete", "DocumentSnapshot successfully deleted!");
                                                            Toast.makeText(getApplicationContext(), "Delete Success!", Toast.LENGTH_LONG).show();
                                                            newsList.remove(position);
                                                            newsAdapter.notifyDataSetChanged();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("delete", "Error deleting document", e);
                                                        }
                                                    });
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