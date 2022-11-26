package com.example.followup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        Log.i("position", String.valueOf(position));

        WebView mWebview = findViewById(R.id.webView);
        mWebview.loadUrl("https://www.google.com/");
    }

    public void onMoreClick(View view) {
        Log.i("position", "onMoreClick");
    }

    public void onAddClick(View view) {
        Log.i("position", "onAddClick");
    }
}