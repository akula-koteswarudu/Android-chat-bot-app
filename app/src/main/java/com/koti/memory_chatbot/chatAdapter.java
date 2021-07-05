package com.koti.memory_chatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class chatAdapter extends ArrayAdapter<message>{

    ArrayList<message> items;
    Context context;


    public chatAdapter(Context context,  ArrayList<message> items) {
        super(context, R.layout.user_message, items);
        this.items = items;
        this.context = context;
    }

    @Nullable
    @Override
    public message getItem(int position) {
        return super.getItem(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        message m = getItem(position);
        String text = m.getText();
        int whos = m.getWhos();

        LayoutInflater layoutInflater= LayoutInflater.from(context);
        TextView textView=null;
        if(whos==0){
            convertView= layoutInflater.inflate(R.layout.user_message,parent,false);
            textView  = convertView.findViewById(R.id.usertext);
        }else if(whos ==1){
            convertView= layoutInflater.inflate(R.layout.bot_message,parent,false);
            textView  = convertView.findViewById(R.id.bottext);
        }
        textView.setText(text);

        return convertView;


    }
}
