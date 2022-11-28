package com.example.followup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        Log.i("position", String.valueOf(position));

        WebView mWebview = findViewById(R.id.webView);
        mWebview.loadUrl("https://www.cbsnews.com/chicago/local-news/");
    }

    public void onMoreClick(View view) {
        Intent i = new Intent(getApplicationContext(), TimelineActivity.class);
        i.putExtra("position", 0);
        startActivity(i);
    }

    public void onAddClick(View view) {
        Toast.makeText(this, "Add To Collections Success!", Toast.LENGTH_LONG).show();
    }
}