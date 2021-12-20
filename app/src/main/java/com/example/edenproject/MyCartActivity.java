package com.example.edenproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class MyCartActivity extends AppCompatActivity implements customAdapter.CheckBoxCheckedListener {
    private ListView listView;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    private ArrayList<String> book_name,check_book;
    private ArrayList<Integer> book_img,check_img_book;
    private Button ProceedToCheckout;
    private customAdapter customAdapter;
    private CheckBox checkBox;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        getSupportActionBar().setTitle("My Cart");
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#c1461d"));
        getSupportActionBar().setBackgroundDrawable(cd);


        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);
        ProceedToCheckout = findViewById(R.id.button_proceed_to_checkout);
        checkBox = findViewById(R.id.checkbox);
        check_book = new ArrayList<>();
        check_img_book = new ArrayList<>();
        book_name =new ArrayList<>();
        book_img = new ArrayList<>();

        authProfile = FirebaseAuth.getInstance();

        firebaseUser = authProfile.getCurrentUser();

        progressBar.setVisibility(View.VISIBLE);
        showUserBooks(firebaseUser);


        //---Pass the books and their images --\\
        ProceedToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCartActivity.this,CheckoutActivity.class);
                intent.putExtra(HomeActivity.EXTRA_BOOKS,check_book);
                intent.putExtra(HomeActivity.EXTRA_IMAGES,check_img_book);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showUserBooks(FirebaseUser firebaseUser) {
        String userID= firebaseUser.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(userID).child("Cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //--- Getting the images and the books name that the user want ---\\
                Map<String,BookItem> map = (Map<String, BookItem>) snapshot.getValue();
                if(map != null) {
                    for (Map.Entry<String, BookItem> book : map.entrySet()) {
                        book_name.add(book.getKey());
                        if (book.getKey().equals("The Last Wish")) {
                            book_img.add(R.drawable.the_witcher_the_last_wish);
                        }
                        if (book.getKey().equals("Sword Of Destiny")) {
                            book_img.add(R.drawable.sword_of_destiny);
                        }
                        if (book.getKey().equals("Blood Of Elves")) {
                            book_img.add(R.drawable.the_witcher_blood_of_elves);

                        }
                        if (book.getKey().equals("The Tower Of The Swallow")) {
                            book_img.add(R.drawable.the_witcher_the_tower_of_the_swallow);
                        }
                    }
                    //--- Sending to the Adapter the current books and images ---\\
                    customAdapter = new customAdapter(MyCartActivity.this,book_name,book_img);
                    listView.setAdapter(customAdapter);
                    customAdapter.setCheckedListener(MyCartActivity.this);

                }
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyCartActivity.this,"Something went wrong ! ",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void getCheckboxCheckedListener(int position) {
        if(check_book != null && check_book.contains(book_name.get(position))){
            check_book.remove(book_name.get(position));
            check_img_book.remove(book_img.get(position));
        }else {
            check_book.add(book_name.get(position));
            check_img_book.add(book_img.get(position));
        }

    }


//--- Menu ToolBar---\\
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_refresh || id == R.id.menu_cart){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if (id == R.id.menu_profile){
            Intent intent = new Intent(MyCartActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_my_list){
            Intent intent = new Intent(MyCartActivity.this,MyListActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_settings){
            Intent intent = new Intent(MyCartActivity.this,SettingsActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_home){
            Intent intent = new Intent(MyCartActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_log_out){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MyCartActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyCartActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}