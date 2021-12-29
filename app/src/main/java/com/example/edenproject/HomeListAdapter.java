package com.example.edenproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private ArrayList<BookItem> bookItems;
    private Context context;
    private Bundle extras;


    public HomeListAdapter(Context context, ArrayList<BookItem> allBooks) {
        this.context = context;
        this.bookItems = allBooks;
    }


    @NonNull
    @Override
    public HomeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeListAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(bookItems.get(position).getUri()).into(holder.imageView);
        holder.textView.setText(bookItems.get(position).getName());
        holder.bookItem = bookItems.get(position);
    }


    @Override
    public int getItemCount() {
        return bookItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageButton imageButtonCurrBook;
        ImageButton imageView;
        private BookItem bookItem;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageButton_image);
            textView = itemView.findViewById(R.id.textView_Text);
            imageButtonCurrBook = imageView.findViewById(R.id.imageButton_image);
            imageButtonCurrBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ChooseBookActivity.class);
                    extras = new Bundle ();
                    extras.putSerializable("BookItem",bookItem);
                    intent.putExtras(extras);
                    context.startActivity(intent);
                }
            });

        }
    }
}
