package com.example.camera;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
//        bottomNavigationView.setSelectedItemId(R.id.itemHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.itemHome: startActivity(new Intent(getApplicationContext(),Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.itemLiveMovie:
                        startActivity(new Intent(getApplicationContext(),LivedVideo.class));
                        overridePendingTransition(0,0);
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
}