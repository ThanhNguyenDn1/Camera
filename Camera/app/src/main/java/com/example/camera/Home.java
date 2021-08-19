package com.example.camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

import static com.example.camera.SendPicture.mSocket;

public class Home extends AppCompatActivity {
     private Emitter.Listener onNewImage;
     private final String SERVER_SEND_IMAGE="SERVER_SEND_IMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        controlItemMenu();
        connect();
    }

    private void connect() {
        try
        {
            mSocket= IO.socket("http://192.168.1.7:3000/");
        }catch(URISyntaxException e)
        {
            e.printStackTrace();
        }
        onNewImage= new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                handleNewImage(args[0]);
            }
        };
        mSocket.connect();
        mSocket.on(SERVER_SEND_IMAGE,onNewImage);
    }

    private void controlItemMenu() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.itemHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.itemHome: return true;
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
    private void handleNewImage(Object arg) {

    }
}