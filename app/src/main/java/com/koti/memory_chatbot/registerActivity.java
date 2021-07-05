package com.koti.memory_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class registerActivity extends AppCompatActivity {
    SQLiteDatabase myDataBase;
    Button register;
    EditText pname;
    EditText fname;
    EditText mname;
    EditText pno1;
    EditText pno2;
    EditText address;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myDataBase = openOrCreateDatabase("chatbot",MODE_PRIVATE,null);
        register = findViewById(R.id.reg);
        pname = findViewById(R.id.pname);
        fname = findViewById(R.id.fname);
        mname = findViewById(R.id.mname);
        pno1 = findViewById(R.id.ph1);
        pno2 = findViewById(R.id.ph2);
        address = findViewById(R.id.add);
        password = findViewById(R.id.password);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkConditions()){
                    myDataBase.execSQL("INSERT INTO REGISTER(pname,fname,mname,pno1,pno2,password,address) VALUES('"+pname.getText().toString()+"','"+fname.getText().toString()+"','"+
                            mname.getText().toString()+"','"+pno1.getText().toString()+"','"+pno2.getText().toString()+"','"+
                            password.getText().toString()+"','"+address.getText().toString()+"')");
                    myDataBase.execSQL("INSERT INTO logins VALUES('"+pname.getText().toString()+"','"+fname.getText().toString()+"','"+
                            mname.getText().toString()+"','"+pno1.getText().toString()+"','"+pno2.getText().toString()+"','"+
                            password.getText().toString()+"','"+address.getText().toString()+"')");
                    Intent intent = new Intent(registerActivity.this,chatActivity.class);
                    myDataBase.close();
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    public boolean checkConditions(){
        if(pname.getText().toString().trim().equals("")){
            Toast.makeText(this,"patient name should not be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(fname.getText().toString().trim().equals("")){
            Toast.makeText(this,"father name should not be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mname.getText().toString().trim().equals("")){
            Toast.makeText(this,"mother name should not be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pno1.getText().toString().trim().length() !=10 || !pno1.getText().toString().trim().matches("[0-9]+")){
            Toast.makeText(this,"phone number should contain 10 digits",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pno2.getText().toString().trim().length() !=10 || !pno2.getText().toString().trim().matches("[0-9]+")){
            Toast.makeText(this,"phone number should contain 10 digits",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(address.getText().toString().trim().equals("")){
            Toast.makeText(this,"address should not be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.getText().toString().trim().length() <8){
            Toast.makeText(this,"password should be at least 8 characters",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
}