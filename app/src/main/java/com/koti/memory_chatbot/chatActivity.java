package com.koti.memory_chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class
chatActivity extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase myDataBase;
    String name;
    ListView chatList;
    chatAdapter adapter;
    EditText chatText;
    Button send;
    String res;
    int sessionId = 1;
    String address;
    String Phone;
    String finalbotText =null;
    private static final int ERROR_DIALOG_REQUEST = 9001;


    public ArrayList<message> array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        myDataBase = openOrCreateDatabase("chatbot",MODE_PRIVATE,null);
        Cursor c= myDataBase.rawQuery("SELECT * FROM logins",null);
        int nameIndex  = c.getColumnIndex("pname");
        int fnameIndex = c.getColumnIndex("fname");
        int mnameIndex = c.getColumnIndex("mname");
        int addIndex = c.getColumnIndex("address");
        int pno1index = c.getColumnIndex("pno1");
        int pno2Index = c.getColumnIndex("pno2");
        c.moveToFirst();
        Toast.makeText(chatActivity.this,"hjghewjjewvjwkjw",Toast.LENGTH_SHORT).show();
        name = c.getString(nameIndex);
        Toast.makeText(this,"hello "+c.getString(nameIndex),Toast.LENGTH_SHORT).show();
        address = c.getString(addIndex);
        address = address.replaceAll("[^a-zA-Z0-9]", "+");
        send = findViewById(R.id.send);
        chatText = findViewById(R.id.edtchat);
        chatList = findViewById(R.id.chatList);

        Cursor d = myDataBase.rawQuery("SELECT count(*) FROM chats",null);
        d.moveToFirst();
        int count = d.getInt(0);
        if(count!=0){
            Cursor m = myDataBase.rawQuery("SELECT user,bot FROM chats WHERE pname ='"+c.getString(nameIndex)+"'",null);
            int userIndex = m.getColumnIndex("user");
            int botIndex = m.getColumnIndex("bot");
            m.moveToFirst();
            while (!m.isAfterLast()){
                String user= m.getString(userIndex);
                String bot= m.getString(botIndex);
                message um = new message(0,user);
                message bm = new message(1,bot);
                array.add(um);
                array.add(bm);
                m.moveToNext();
            }
        }

        adapter = new chatAdapter(chatActivity.this,array);
        chatList.setAdapter(adapter);


        sendInfo(c.getString(nameIndex),c.getString(fnameIndex),c.getString(mnameIndex),c.getString(pno1index),c.getString(pno2Index),c.getString(addIndex));

        send.setOnClickListener(chatActivity.this);
    }

    private void sendInfo(String name, String fname, String mname, String pno1, String pno2, String address) {
        OkHttpClient okHttpClient= new OkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("name",name).add("fname",fname).add("mname",mname).add("pno1",pno1).
                add("pno2",pno2).add("address",address).add("sessionid",String.valueOf(sessionId)).build();
        Request request = new Request.Builder().url("http://192.168.43.89:5000/info").post(formBody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("successfully sent");
            }
        });

    }

    public boolean isServiceOk(){
        Log.d("chatActivity","isServiceOk: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(chatActivity.this);
        if(available == ConnectionResult.SUCCESS){
            Log.d("chatActivity","isservicesOk: google play services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d("chatActivity","isServicesOk: we can fix this error");
            Handler mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    // This is where you do your work in the UI thread.
                    // Your worker tells you in the message what to do.
                    Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(chatActivity.this,available,ERROR_DIALOG_REQUEST);
                    dialog.show();
                }
            };
            mHandler.handleMessage(null);
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(chatActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(chatActivity.this,"we can make map requests",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                myDataBase.execSQL("DELETE FROM logins");
                Intent intent = new Intent(chatActivity.this,LoginActivity.class);
                myDataBase.close();
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }


    }

    @Override
    public void onClick(View view) {

        View view1 = chatActivity.this.getCurrentFocus();
        if(view1!=null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

        String text = chatText.getText().toString().trim();
        String tempText =text;
        array.add(new message(0,text));
        adapter.notifyDataSetChanged();
        text = text.replaceAll("[^a-zA-Z0-9\\s]", "");
        text =text.toLowerCase();
        OkHttpClient okHttpClient= new OkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("value",text).add("sessionid",String.valueOf(sessionId)).build();
        Request request = new Request.Builder().url("http://192.168.43.89:5000/post").post(formBody).build();
        String finalText = text;
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("failed");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                res = response.body().string();

                if(res.equals("open maps") && isServiceOk()){
                    array.add(new message(1,"opening maps..."));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myDataBase.execSQL("INSERT INTO chats VALUES('"+name+"','"+tempText.replace("'","")+"','"+
                                    "opening maps..."+"')");
                            adapter.notifyDataSetChanged();
                            chatText.setText("");
                            Uri gmmIntentUri = Uri.parse("google.navigation:q="+address);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    });
                }
                else if(res.equals("open google")){
                    array.add(new message(1,"opening google..."));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myDataBase.execSQL("INSERT INTO chats VALUES('"+name+"','"+tempText.replace("'","")+"','"+
                                    "opening google..."+"')");
                            String finText = finalText.replace("google","");
                            finText = finText.replaceAll("[^a-zA-Z0-9]", "+");
                            adapter.notifyDataSetChanged();
                            chatText.setText("");
                            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com/search?q="+ finText));
                            startActivity(intent);
                        }
                    });
                }
                else if(res.equals("open app")){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String finText = finalText.replace("open","");
                            chatText.setText("");
                            myDataBase.execSQL("INSERT INTO chats VALUES('"+name+"','"+tempText.replace("'","")+"','"+
                                    "opening "+finText.replace("'","")+"... using google"+"')");
                            array.add(new message(1,"opening "+finText+"... using google"));
                            finText = finText.replaceAll("[^a-zA-Z0-9]", "+");
                            adapter.notifyDataSetChanged();
                            chatText.setText("");
                            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com/search?q="+ finText));
                            startActivity(intent);
                        }
                    });

                }
                else if(res.matches("call [\\d]*")){
                    System.out.println("hi");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String finText = res.replace("call ","");
                            chatText.setText("");
                            array.add(new message(1,"calling "+finText+"...."));
                            myDataBase.execSQL("INSERT INTO chats VALUES('"+name+"','"+tempText.replace("'","")+"','"+
                                    "calling "+finText.replace("'","")+"...."+"')");
                            adapter.notifyDataSetChanged();
                            chatText.setText("");
                            Phone = finText;
                            if(ContextCompat.checkSelfPermission(chatActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(chatActivity.this,new String[]{Manifest.permission.CALL_PHONE},101);
                            }else{
                                makePhoneCall(finText);
                            }
                        }
                    });
                }
                else{
                    finalbotText =res;
                    array.add(new message(1,res));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myDataBase.execSQL("INSERT INTO chats VALUES('"+name+"','"+tempText+"','"+
                                    res.replace("'","")+"')");
                            adapter.notifyDataSetChanged();
                            chatText.setText("");
                        }
                    });
                }

            }
        });


    }

    public void makePhoneCall(String phone){
        String dial = "tel:"+phone;
        startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==101){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                makePhoneCall(Phone);
            }else{
                Toast.makeText(chatActivity.this,"permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
