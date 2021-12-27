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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListAdapterNewBook extends ArrayAdapter<ArrayList<BookItem>> {
    private final Context context;
    private ArrayList<BookItem> book_name;


        public ListAdapterNewBook(@NonNull Context context, ArrayList data) {
            super(context, R.layout.list_item2, data);
            book_name = new ArrayList();
            this.context = context;
            this.book_name = data;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row =convertView;
            com.example.edenproject.ViewHolder holder =null;


            if (row == null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.list_item2,parent,false);
                holder = new com.example.edenproject.ViewHolder(row);
                row.setTag(holder);
            }else{
                holder= (com.example.edenproject.ViewHolder) row.getTag();
            }
                Glide.with(context).load(book_name.get(position).getUri()).into(holder.imageView);
                holder.textView.setText(book_name.get(position).getName());
                holder.Author_name.setText(book_name.get(position).getAuthorName());
                holder.Category.setText(book_name.get(position).getCategory());
                holder.Pages.setText(Integer.toString(book_name.get(position).getPages()));
                holder.Publication_date.setText(book_name.get(position).getPublicationDate());

            return row;
        }


    }


