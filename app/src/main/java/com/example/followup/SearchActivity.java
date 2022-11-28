package com.example.followup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void onSearchClick(View view) {
        //add list
        ListView mListView = findViewById(R.id.news_list);
        mListView.setAdapter(new NewsAdapter(this));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView arg0, View arg1, int
                    position,long arg3) {
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                i.putExtra("position", position);
                startActivity(i);
            }
        });
    }
}