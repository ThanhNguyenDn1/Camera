package com.example.camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LivedVideo extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lived_video);
        controlItemMenu();
        addControl();
        loadVideo();
    }
    private void controlItemMenu(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.itemLiveMovie);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.itemHome:
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.itemLiveMovie:
                        return true;
                    case R.id.itemSendPicture:
                        startActivity(new Intent(getApplicationContext(),SendPicture.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }
    private void addControl(){
        webView=(WebView) findViewById(R.id.webView);
    }
    private  void loadVideo(){
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://192.168.1.100:3000/client");
        WebSettings webSettings = webView.getSettings();
//        webSettings.setBuiltInZoomControls(false);
//        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
    }


}