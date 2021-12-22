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

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ArrayList<String>> {

        private final Context context;
        private ArrayList<String> book_name;
        private ArrayList<Integer> imgid;

        public ListAdapter(@NonNull Context context, ArrayList data, ArrayList img) {
            super(context, R.layout.list_item2, data);
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
            ViewHolder holder =null;


            if (row == null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.list_item2,parent,false);
                holder = new ViewHolder(row);
                row.setTag(holder);
            }else{
                holder= (ViewHolder) row.getTag();
            }
            holder.imageView.setImageResource(imgid.get(position));
            holder.textView.setText(book_name.get(position));
            return row;
        }


    }
    class ViewHolder{
        ImageView imageView;
        TextView textView;

        ViewHolder(View view){
            imageView = view.findViewById(R.id.imageView_book);
            textView = view.findViewById(R.id.textView_book_name);
        }
    }


