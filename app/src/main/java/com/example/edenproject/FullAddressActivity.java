package com.example.edenproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class FullAddressActivity extends AppCompatActivity {
    private TextView text_fullname,text_county,text_city,text_street,text_postal_code;
    private Button button_back_to_profile,button_edit_address;
    private String fullname,country,city,street,postal_code;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_address);

        getSupportActionBar().setTitle("Address Details");

        button_edit_address = findViewById(R.id.button_edit_address);

        button_edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FullAddressActivity.this,AddressActivity.class));
                finish();
            }
        });

        text_fullname = findViewById(R.id.textView_show_full_name);
        text_county = findViewById(R.id.textView_show_country);
        text_city = findViewById(R.id.textView_show_city);
        text_street = findViewById(R.id.textView_show_street);
        text_postal_code = findViewById(R.id.textView_show_postal_code);

        progressBar = findViewById(R.id.progressBar);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(FullAddressActivity.this,"Something went wrong ! User's details are not available",Toast.LENGTH_LONG).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            showUserAddress(firebaseUser);
        }


    }

    private void showUserAddress(FirebaseUser firebaseUser) {
        String userID= firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserClass ReadUserDetails = snapshot.getValue(UserClass.class);
                fullname= ReadUserDetails.getFull_name();
                text_fullname.setText(fullname);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FullAddressActivity.this,"Something went wrong ! ",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });


        databaseReference.child(userID).child("Address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AddressClass ReadUserAddress = snapshot.getValue(AddressClass.class);
                if(ReadUserAddress != null){
                    country = ReadUserAddress.getCounty();
                    city = ReadUserAddress.getCity();
                    street = ReadUserAddress.getStreet();
                    postal_code = ReadUserAddress.getPostal_Code();
                    text_county.setText(country);
                    text_city.setText(city);
                    text_street.setText(street);
                    text_postal_code.setText(postal_code);


                }else{
                    country ="None";
                    city = "None";
                    street = "None";
                    postal_code = "None";
                    text_county.setText(country);
                    text_city.setText(city);
                    text_street.setText(street);
                    text_postal_code.setText(postal_code);

                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FullAddressActivity.this,"Something went wrong ! ",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_refresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if (id == R.id.menu_profile){
            Intent intent = new Intent(FullAddressActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_my_list){
            Intent intent = new Intent(FullAddressActivity.this,MyListActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_settings){
            Intent intent = new Intent(FullAddressActivity.this,SettingsActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_log_out){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(FullAddressActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FullAddressActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_home){
            startActivity(new Intent(FullAddressActivity.this, HomeActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}