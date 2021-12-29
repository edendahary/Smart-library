package com.example.edenproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewHomeActivity extends AppCompatActivity {
    RecyclerView recyclerViewDrama,recyclerViewAction,recyclerViewAllBooks;
    HomeListAdapter homeListAdapter;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private ArrayList<BookItem> AllBooks,DramaBooks,ActionBooks;
    private BookItem bookItem;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Home");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#c1461d"));
        getSupportActionBar().setBackgroundDrawable(cd);

        AllBooks = new ArrayList<>();
        DramaBooks = new ArrayList<>();
        ActionBooks = new ArrayList<>();

        recyclerViewDrama = findViewById(R.id.recycler_view);
        recyclerViewAction = findViewById(R.id.recycler_view1);
        recyclerViewAllBooks = findViewById(R.id.recycler_view2);


        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("AllBooks");
        referenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        bookItem = dataSnapshot.getValue(BookItem.class);
                        if(bookItem.getCategory().toLowerCase().equals("drama")){
                            DramaBooks.add(bookItem);
                        }else if(bookItem.getCategory().toLowerCase().equals("action")){
                            ActionBooks.add(bookItem);
                        }
                        AllBooks.add(bookItem);
                    }
                    if(DramaBooks.size()!=0){
                        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(
                                NewHomeActivity.this,LinearLayoutManager.HORIZONTAL,false);
                        recyclerViewDrama.setLayoutManager(linearLayoutManager);
                        recyclerViewDrama.setItemAnimator(new DefaultItemAnimator());
                        homeListAdapter = new HomeListAdapter(NewHomeActivity.this,DramaBooks);
                        recyclerViewDrama.setAdapter(homeListAdapter);
                    }
                    if(ActionBooks.size()!=0) {

                        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(
                                NewHomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewAction.setLayoutManager(linearLayoutManager1);
                        recyclerViewAction.setItemAnimator(new DefaultItemAnimator());
                        homeListAdapter = new HomeListAdapter(NewHomeActivity.this, ActionBooks);
                        recyclerViewAction.setAdapter(homeListAdapter);
                    }

                    if(AllBooks.size()!=0) {
                        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(
                                NewHomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewAllBooks.setLayoutManager(linearLayoutManager2);
                        recyclerViewAllBooks.setItemAnimator(new DefaultItemAnimator());
                        homeListAdapter = new HomeListAdapter(NewHomeActivity.this, AllBooks);
                        recyclerViewAllBooks.setAdapter(homeListAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NewHomeActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });







    }



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
        if(id == R.id.menu_refresh || id == R.id.menu_home){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if (id == R.id.menu_new_book){
            Intent intent = new Intent(NewHomeActivity.this,AddBooksActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_profile){
            Intent intent = new Intent(NewHomeActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_my_list){
            Intent intent = new Intent(NewHomeActivity.this,MyListActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_settings){
            Intent intent = new Intent(NewHomeActivity.this,SettingsActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_cart){
            Intent intent = new Intent(NewHomeActivity.this,MyCartActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_log_out){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(NewHomeActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NewHomeActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}