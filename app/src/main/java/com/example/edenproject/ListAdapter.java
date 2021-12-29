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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<BookItem> {

        private final Context context;
        private ArrayList<BookItem> books;

        public ListAdapter(@NonNull Context context, ArrayList<BookItem> data) {
            super(context, R.layout.list_item2, data);
            books = new ArrayList();
            this.context = context;
            this.books = data;
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
            Glide.with(context).load(books.get(position).getUri()).into(holder.imageView);
            holder.textView.setText(books.get(position).getName());
            return row;
        }


    }
    class ViewHolder{
        ImageView imageView;
        TextView textView,Author_name,Category,Publication_date,Pages;

        ViewHolder(View view){
            imageView = view.findViewById(R.id.imageView_book);
            textView = view.findViewById(R.id.textView_book_name);
            Author_name = view.findViewById(R.id.textView_book_author_name);
            Category = view.findViewById(R.id.textView_book_category);
            Publication_date = view.findViewById(R.id.textView_book_publication_date);
            Pages =  view.findViewById(R.id.textView_book_pages);
        }
    }


