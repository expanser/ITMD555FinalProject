package com.example.followup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class CollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        ListView mListView = findViewById(R.id.news_list);
        mListView.setAdapter(new NewsAdapter(this));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView arg0, View arg1, int
                    position, long arg3) {
                Log.i("click", String.valueOf(position));
//                Intent i = new Intent(getActivity().getApplicationContext(), NewsDetail.class);
//                i.putExtra("position", position);
//                startActivity(i);
            }
        });
    }
}