package com.example.edenproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
//-- List on CheckOut Activity --\\
public class ListAdapter extends ArrayAdapter<BookItem> {

        private final Context context;
        private ArrayList<BookItem> books;
        private DatabaseReference databaseReference;
        private FirebaseUser firebaseUser;
        private FirebaseAuth firebaseAuth;

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
            holder.Author_name.setText(books.get(position).getAuthorName());
            holder.Category.setText(books.get(position).getCategory());
            holder.Pages.setText(Integer.toString(books.get(position).getPages()));
            holder.Publication_date.setText(books.get(position).getPublicationDate());
            holder.Price.setText(Integer.toString(books.get(position).getPrice()));
            holder.imageButton_Delete.setVisibility(View.GONE);

            return row;
        }



    }
    class ViewHolder{
        ImageView imageView;
        TextView textView,Author_name,Category,Publication_date,Pages,Price,Sold_amount;
        ImageButton imageButton_Delete;

        ViewHolder(View view){
            imageView = view.findViewById(R.id.imageView_book);
            textView = view.findViewById(R.id.textView_book_name);
            Author_name = view.findViewById(R.id.textView_book_author_name);
            Category = view.findViewById(R.id.textView_book_category);
            Publication_date = view.findViewById(R.id.textView_book_publication_date);
            Pages =  view.findViewById(R.id.textView_book_pages);
            imageButton_Delete = view.findViewById(R.id.button_delete_book);
            Price = view.findViewById(R.id.textView_book_author_price);
            Sold_amount = view.findViewById(R.id.textView_book_author_sold_count);

        }
    }


