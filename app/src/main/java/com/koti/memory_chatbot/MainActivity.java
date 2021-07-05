package com.koti.memory_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity {
    ImageView imageView ;
    TextView greet;
    TextView tag;
    Animation topAnim,bottomAnim;
     SQLiteDatabase myDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        myDataBase = openOrCreateDatabase("chatbot",MODE_PRIVATE,null);
        myDataBase.execSQL("CREATE TABLE IF NOT EXISTS logins (pname VARCHAR,fname VARCHAR,mname VARCHAR, pno1 VARCHAR,pno2 VARCHAR,password VARCHAR,address VARCHAR)");
        myDataBase.execSQL("CREATE TABLE IF NOT EXISTS register (pname VARCHAR,fname VARCHAR,mname VARCHAR, pno1 VARCHAR,pno2 VARCHAR,password VARCHAR,address VARCHAR)");
        myDataBase.execSQL("CREATE TABLE IF NOT EXISTS chats (pname VARCHAR,user VARCHAR,bot VARCHAR)");
        imageView = findViewById(R.id.splashimage);
        greet = findViewById(R.id.greet);
        tag = findViewById(R.id.desc);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        imageView.setAnimation(topAnim);
        greet.setAnimation(topAnim);
        tag.setAnimation(bottomAnim);

        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Cursor c = myDataBase.rawQuery("SELECT count(*) FROM logins",null);
                c.moveToFirst();
                int count = c.getInt(0);
                Intent intent;
                if(count!=0){
                    intent = new Intent(MainActivity.this, chatActivity.class);
                }else{
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                myDataBase.close();
                startActivity(intent);
                finish();
            }
        }.start();


    }
}