package com.koti.memory_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    ImageView openeye ;
    EditText patName;
    EditText password;
    Button login;
    Button register;
    boolean hide= true;
    SQLiteDatabase myDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDataBase = openOrCreateDatabase("chatbot",MODE_PRIVATE,null);
        openeye = findViewById(R.id.openeye);
        patName = findViewById(R.id.patName);
        password = findViewById(R.id.pass);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        login = findViewById(R.id.login);
        register= findViewById(R.id.register);

        openeye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hide){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    openeye.setImageResource(R.drawable.openeye);
                    hide=false;
                }
                else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    openeye.setImageResource(R.drawable.closedeye);
                    hide=true;
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(LoginActivity.this, registerActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c= myDataBase.rawQuery("SELECT pname,password FROM register",null);
                Cursor c1 = myDataBase.rawQuery("SELECT count(*) FROM register",null);
                c1.moveToFirst();
                int count = c1.getInt(0);boolean b=false;
                if(count==0)
                    Toast.makeText(LoginActivity.this,"invalid credentials",Toast.LENGTH_SHORT).show();
                else{
                    c.moveToFirst();
                    int nameIndex = c.getColumnIndex("pname");
                    int passIndex = c.getColumnIndex("password");
                    while (!c.isAfterLast()){
                        if(c.getString(nameIndex).equals(patName.getText().toString()) && c.getString(passIndex).equals(password.getText().toString())){
                            myDataBase.execSQL("INSERT INTO logins VALUES('"+patName.getText().toString()+"')");
                            Intent intent = new Intent(LoginActivity.this,chatActivity.class);
                            myDataBase.close();
                            startActivity(intent);
                            finish();
                            b=true;
                        }
                        c.moveToNext();
                    }
                    if(!b)
                        Toast.makeText(LoginActivity.this,"invalid credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


}