package com.example.followup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = FirebaseFirestore.getInstance();
    }

    public void onSearchClick(View view) {
        EditText title;
        title = findViewById(R.id.editTextTitle);

        if (title.getText().length() == 0) {
            Toast.makeText(this, "Please enter a keyword for searching",
                    Toast.LENGTH_LONG).show();
            return;
        }

        //due to the fact that firebase free version doesn't support LIKE operation, here I use "whereEqualTo" instead.
        db.collection("newslist")
                .whereEqualTo("title", title.getText().toString())
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
                        } else {
                            Log.w("onFailure", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

}