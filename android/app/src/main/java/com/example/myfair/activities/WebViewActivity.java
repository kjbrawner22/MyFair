package com.example.myfair.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.myfair.R;

public class WebViewActivity extends AppCompatActivity {
    public static final String VIEW_URL = "view_url";
    public static final String TOOLBAR_TITLE = "toolbar_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView webView = findViewById(R.id.webview);

        Intent intent = getIntent();

        String url = intent.getStringExtra(VIEW_URL);
        if (url != null) {
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
        } else {
            finish();
        }

        setupToolbar(intent.getStringExtra(TOOLBAR_TITLE), webView.getUrl());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupToolbar(String title, String url) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (title != null) {
            toolbar.setTitle(title);
        }
        toolbar.setSubtitle(url);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        Log.d("ACTION_BAR_CARD_INFO", "Actionbar: " + actionBar);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
}
