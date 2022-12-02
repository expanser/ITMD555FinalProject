package com.example.followup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class ArticleActivity extends AppCompatActivity {

    String id;
    String link;
    String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Intent i = getIntent();
        id = i.getStringExtra("id");
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
        Toast.makeText(this, "Add To Collections Success!", Toast.LENGTH_LONG).show();
    }
}