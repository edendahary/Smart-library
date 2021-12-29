package com.example.edenproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseBookActivity extends AppCompatActivity {
    private Button buttonAddToCart;
    private TextView textViewBookTitle;
    private ImageView imageView;
    private Bundle extras;
    private Bitmap image;
    private BookItem bookitem;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private DatabaseReference referenceProfile;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_book);
        getSupportActionBar().setTitle("Choose Book");
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#c1461d"));
        getSupportActionBar().setBackgroundDrawable(cd);


        authProfile = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = authProfile.getCurrentUser();


        //--- Buttons ---\\
        buttonAddToCart = findViewById(R.id.button_add_to_cart);

        textViewBookTitle = findViewById(R.id.textView_book_title);


        //--- Get the Image Drawable ---\\

//        if (bundle != null) {
//
//            imageView = findViewById(R.id.imageView_profile_pic);
//
//            image = bundle.getParcelable("EXTRA_IMAGE");
//            imageView.setImageBitmap(image);
//
//        }

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            imageView = findViewById(R.id.imageView_profile_pic);
            bookitem = (BookItem) bundle.getSerializable("BookItem");
            Glide.with(this).load(bookitem.getUri()).into(imageView);
            textViewBookTitle.setText(bookitem.getName());

        }


        //--- Get the Text ---\\
//        Intent intent = getIntent();
//        String text = intent.getStringExtra(HomeActivity.EXTRA_TEXT);
       // textViewBookTitle.setText(text);

        buttonAddToCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //--- Add books to the cart in the database ---\\
                referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
                referenceProfile.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            referenceProfile.child(firebaseUser.getUid()).child("Cart").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists() && snapshot.hasChild(bookitem.getName())) {
                                        for (DataSnapshot dataSnapshot : snapshot.child(bookitem.getName()).getChildren()) {
                                            if (dataSnapshot.getValue().toString().equals(bookitem.getName())) {
                                                bookitem.setName((String) dataSnapshot.getValue());
                                            }
                                            if (dataSnapshot.getKey().equals("quantity")) {
                                                bookitem.setQuantity((int) ((Long) dataSnapshot.getValue() + 1));
                                            }
                                        }
                                    } else {
                                        referenceProfile.child(firebaseUser.getUid()).child("Cart").child(bookitem.getName()).setValue(bookitem);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            DatabaseReference reference = referenceProfile.child("Authors").child(firebaseUser.getUid()).child("Cart");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists() && snapshot.hasChild(bookitem.getName())) {
                                        for (DataSnapshot dataSnapshot : snapshot.child(bookitem.getName()).getChildren()) {
                                            if (dataSnapshot.getValue().toString().equals(bookitem.getName())) {
                                                bookitem.setName((String) dataSnapshot.getValue());
                                            }
                                            if (dataSnapshot.getKey().equals("quantity")) {
                                                bookitem.setQuantity((int) ((Long) dataSnapshot.getValue() + 1));
                                            }
                                        }
                                    } else {
                                        referenceProfile.child("Authors").child(firebaseUser.getUid()).child("Cart").child(bookitem.getName()).setValue(bookitem);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
        if(id == R.id.menu_refresh ){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if (id == R.id.menu_new_book){
            Intent intent = new Intent(ChooseBookActivity.this,AddBooksActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_home){
            Intent intent = new Intent(ChooseBookActivity.this,NewHomeActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_profile){
            Intent intent = new Intent(ChooseBookActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_my_list){
            Intent intent = new Intent(ChooseBookActivity.this,MyListActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_settings){
            Intent intent = new Intent(ChooseBookActivity.this,SettingsActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_cart){
            Intent intent = new Intent(ChooseBookActivity.this,MyCartActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_log_out){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(ChooseBookActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChooseBookActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}