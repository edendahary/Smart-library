package com.example.edenproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class customAdapter extends ArrayAdapter <ArrayList<String>> {
    private final Context context;
    private ArrayList<String> book_name;
    private ArrayList<Integer> imgid;

    public customAdapter(@NonNull Context context, ArrayList data, ArrayList img) {
        super(context, R.layout.list_item,data);
        System.out.println(data.size());
        book_name =new ArrayList();
        imgid = new ArrayList();
        this.context= context;
        this.book_name= data;
        this.imgid=img;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }


        ImageView img = convertView.findViewById(R.id.imageView_book);
        TextView name = convertView.findViewById(R.id.textView_book_name);

        img.setImageResource(imgid.get(position));
        name.setText(book_name.get(position));

        return convertView;
    }
}
