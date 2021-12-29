package com.example.edenproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyListActivity extends AppCompatActivity {
    private ListView listView;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private BookItem bookItem;
    private ArrayList<BookItem> books;
    private ListAdapterNewBook listAdapterNewBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        getSupportActionBar().setTitle("My List");
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#c1461d"));
        getSupportActionBar().setBackgroundDrawable(cd);
        books =new ArrayList<>();


        listView = findViewById(R.id.listView2);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();

        firebaseUser = authProfile.getCurrentUser();

        progressBar.setVisibility(View.VISIBLE);
        showAuthorBooks(firebaseUser);



    }

    private void showAuthorBooks(FirebaseUser firebaseUser) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child("Authors").child(firebaseUser.getUid()).child("Books").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    bookItem = child.getValue(BookItem.class);
                    books.add(bookItem);
                }
                ShowOnList(books);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyListActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void ShowOnList(ArrayList<BookItem> books) {
        listAdapterNewBook = new ListAdapterNewBook(getApplicationContext(),books);
        listView.setAdapter(listAdapterNewBook);
        progressBar.setVisibility(View.GONE);
    }


    //--- Menu ToolBar---\\
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userID= firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child(userID).exists()){
                    getMenuInflater().inflate(R.menu.author_menu,menu);
                }else {
                    getMenuInflater().inflate(R.menu.common_menu,menu);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_refresh ||id == R.id.menu_my_list){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if(id == R.id.menu_new_book){
            Intent intent = new Intent(MyListActivity.this,AddBooksActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_profile){
            Intent intent = new Intent(MyListActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_settings){
            Intent intent = new Intent(MyListActivity.this,SettingsActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_log_out){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MyListActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyListActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_home){
            startActivity(new Intent(MyListActivity.this, NewHomeActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}