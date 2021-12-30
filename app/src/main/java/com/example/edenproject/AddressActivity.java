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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

public class AddressActivity extends AppCompatActivity {
    private TextView textViewAddress_Country,textViewFullName,textViewAddress_City,textViewAddress_Street,textViewAddress_Postal_Code;
    private String fullname;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    private Button add_Address;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#c1461d"));
        getSupportActionBar().setBackgroundDrawable(cd);
        getSupportActionBar().setTitle("My Address");


        //textViewAddress = findViewById(R.id.textView_show_address_details);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewAddress_Country = findViewById(R.id.editText_Address_County);
        textViewAddress_City = findViewById(R.id.editText_Address_City);
        textViewAddress_Street = findViewById(R.id.editText_Address_Street);
        textViewAddress_Postal_Code = findViewById(R.id.editText_Address_Postal_Code);
        add_Address = findViewById(R.id.button_add_address);

        progressBar = findViewById(R.id.progressBar);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(AddressActivity.this,"Something went wrong ! User's details are not available",Toast.LENGTH_LONG).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }



    }
    private void showUserProfile(FirebaseUser firebaseUser){
        String userID= firebaseUser.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserClass ReadUserDetails = snapshot.getValue(UserClass.class);
                if(ReadUserDetails != null){
                    fullname= ReadUserDetails.getFull_name();
                    textViewFullName.setText(fullname);
                    progressBar = findViewById(R.id.progressBar);

                }
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddressActivity.this,"Something went wrong ! ",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });

        add_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_Country = textViewAddress_Country.getText().toString();
                String text_City = textViewAddress_City.getText().toString();
                String text_Street = textViewAddress_Street.getText().toString();
                String text_Postal_Code = textViewAddress_Postal_Code.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                AddressClass addressClass = new AddressClass(text_Country,text_City,text_Street,text_Postal_Code);

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
                referenceProfile.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            referenceProfile.child(firebaseUser.getUid()).child("Address").setValue(addressClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    startActivity(new Intent(AddressActivity.this,FullAddressActivity.class));
                                    finish();
                                }
                            });
                        }else {
                            referenceProfile.child("Authors").child(firebaseUser.getUid()).child("Address").setValue(addressClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    startActivity(new Intent(AddressActivity.this,FullAddressActivity.class));
                                    finish();
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
        if(id == R.id.menu_refresh ){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if(id == R.id.menu_my_list){
            Intent intent = new Intent(AddressActivity.this,MyListActivity.class);
            startActivity(intent);
            finish();
        }else if(id == R.id.menu_new_book){
            Intent intent = new Intent(AddressActivity.this,AddBooksActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_profile){
            Intent intent = new Intent(AddressActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_settings){
            Intent intent = new Intent(AddressActivity.this,SettingsActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_cart){
            Intent intent = new Intent(AddressActivity.this,MyCartActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.menu_log_out){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(AddressActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddressActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_home){
            startActivity(new Intent(AddressActivity.this, NewHomeActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}