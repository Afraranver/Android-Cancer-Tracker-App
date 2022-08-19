package com.example.cancer_track;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.Objects;

public class InfoActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Objects.requireNonNull(getSupportActionBar()).hide();

        webView = findViewById(R.id.webview);
        WebView theWebPage = new WebView(this);
        theWebPage.getSettings().setJavaScriptEnabled(true);
        theWebPage.getSettings().setPluginState(WebSettings.PluginState.ON);
        setContentView(theWebPage);
        theWebPage.loadUrl("https://www.cclg.org.uk/Types-of-childhood-cancer");


    }
}