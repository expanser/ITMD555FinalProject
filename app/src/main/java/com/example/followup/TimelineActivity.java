package com.example.followup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TimelineActivity extends AppCompatActivity {

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

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        Log.i("position", String.valueOf(position));

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
}