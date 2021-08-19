package com.example.camera;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SendPicture extends AppCompatActivity {


    private final String CLIENT_SEND_IMAGE="CLIENT_SEND_IMAGE";
    public static Socket mSocket;
    private Bitmap bitmap;
    private int REQUEST_CODE_PICTURE=1;
    private int REQUEST_CODE_CAMERA=2;
    private TextView textView;
    private ImageButton imageButtonCamera, imageButtonPicture,imageButtonUpLoad;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controlItemMenu();
        connect();
        addControl();
        setEvent();
    }
    private void sendImage() {
        if(isOnline()==true){
            if(bitmap!=null)
            {
                byte[] bytes=getbyteArrayFromBitmap(bitmap);
                mSocket.emit(CLIENT_SEND_IMAGE,bytes);
                textView.setText("");
                Bitmap bitmapview=BitmapFactory.decodeResource(getResources(),R.mipmap.hi4);
                imageView.setImageBitmap(bitmapview);
                bitmap=null;
            }
            else if(bitmap==null)
            {
                Bitmap bitmapview=BitmapFactory.decodeResource(getResources(),R.mipmap.hi5);
                imageView.setImageBitmap(bitmapview);
            }
        }
        else{
            Bitmap bitmapview=BitmapFactory.decodeResource(getResources(),R.mipmap.hi6);
            imageView.setImageBitmap(bitmapview);
        }
    }
    private void choosePicture() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE_PICTURE);
    }
    private  void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CODE_CAMERA);
    }
    private void controlItemMenu() {
        setContentView(R.layout.activity_send_picture);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.itemSendPicture);
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
                        startActivity(new Intent(getApplicationContext(),LivedVideo.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.itemSendPicture:
                        return true;
                }

                return false;
            }
        });
    }
    private void connect() {
        try
        {
            mSocket= IO.socket("http://192.168.1.100:3000/");
        }catch(URISyntaxException e)
        {
            e.printStackTrace();
        }

        mSocket.connect();
    }
    private void addControl() {
        imageButtonCamera=findViewById(R.id.imageButtonCamera);
        imageButtonPicture=findViewById(R.id.imageButtonPicture);
        imageButtonUpLoad=findViewById(R.id.imageButtonUpLoad);
        imageView=findViewById(R.id.imageView);
        textView = findViewById(R.id.tvIfo);

    }
    public byte[] getbyteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[]byteArray=stream.toByteArray();
        return byteArray;
    }
    private void setEvent() {
        imageButtonPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        imageButtonUpLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                sendImage();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE_PICTURE && resultCode==RESULT_OK && data!=null)
        {
            try {
                Uri uri=data.getData();
                InputStream inputStream=getContentResolver().openInputStream(uri);
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inJustDecodeBounds=true;
                BitmapFactory.decodeStream(inputStream,null,options);
        //        Log.d("AAAAA", "hi: "+options.outHeight+"  "+options.outWidth);
                options.inSampleSize= calulateInSampleSize(options,1000,1000);
                options.inJustDecodeBounds=false;
                bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(uri),null,options);
                imageView.setImageBitmap(bitmap);
                setTextView();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        else if(requestCode==REQUEST_CODE_CAMERA && resultCode==RESULT_OK && data!=null)
        {
            bitmap= (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            setTextView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void setTextView(){
        int a=bitmap.getHeight();
        int b=bitmap.getWidth();
        double c=(double) a*b/1000000;
        textView.setText(bitmap.getWidth()+" x "+bitmap.getHeight()+"   "+((double)Math.round(c*10)/10)+"MP");
    }
    private Boolean isOnline(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected())
        {
            return true;
        }
        return false;
    }
    private int calulateInSampleSize(BitmapFactory.Options options,int reWidth, int reHeight){
        int height=options.outHeight;
        int width=options.outWidth;
//        Log.d("AAAA", "calulateInSampleSize: "+height+"  "+width);
        int inSampleSize=1;
        if(height>reHeight||width>reWidth){
            int halfheight=height/2;
            int halfWidth=width/2;
            while((halfheight/inSampleSize)>=reHeight&&(halfWidth/inSampleSize)>=reWidth){
                inSampleSize *=2;
            }
        }
        return inSampleSize;
    }
}