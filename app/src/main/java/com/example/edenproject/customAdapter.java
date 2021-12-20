package com.example.edenproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import java.nio.file.StandardWatchEventKinds;
import java.util.ArrayList;
import java.util.Arrays;

public class customAdapter extends ArrayAdapter <ArrayList<String>> {
    private final Context context;
    private ArrayList<String> book_name;
    private ArrayList<Integer> imgid;
    private CheckBoxCheckedListener checkedListener;

    public customAdapter(@NonNull Context context, ArrayList data, ArrayList img) {
       super(context, R.layout.list_item, data);
        book_name = new ArrayList();
        imgid = new ArrayList();
        this.context = context;
        this.book_name = data;
        this.imgid = img;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row =convertView;
        MyViewHolder holder =null;


        if (row == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item,parent,false);
            holder = new MyViewHolder(row);
            row.setTag(holder);
        }else{
            holder= (MyViewHolder) row.getTag();
        }
        holder.imageView.setImageResource(imgid.get(position));
        holder.textView.setText(book_name.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkedListener != null){
                    checkedListener.getCheckboxCheckedListener(position);
                }
            }
        });
        return row;
    }

    public interface CheckBoxCheckedListener {
        void getCheckboxCheckedListener(int position);
    }

    public void setCheckedListener(CheckBoxCheckedListener checkedListener) {
        this.checkedListener = checkedListener;
    }
}
class MyViewHolder{
    ImageView imageView;
    CheckBox checkBox;
    TextView textView;

    MyViewHolder(View view){
        imageView = view.findViewById(R.id.imageView_book);
        textView = view.findViewById(R.id.textView_book_name);
        checkBox= view.findViewById(R.id.checkbox);
    }
}
