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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity{
    private String fullName,Street,City,County,Phone,Zip;
    private TextView textViewFullName,textViewStreet,textViewCity,textViewCounty,textViewMobile,textViewZip;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    private ListView listView;
    private ListAdapter listAdapter;
    private ArrayList<BookItem>books;
    private Button buttonNewCard;
    private RadioButton RadiobuttonCreditCard,RadiobuttonPaypal,RadiobuttonBit;
    private EditText editTextPaypal,editTextBit;
    private DatabaseReference databaseReference;
    private Bundle extras;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setTitle("Checkout");
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#c1461d"));
        getSupportActionBar().setBackgroundDrawable(cd);

        progressBar = findViewById(R.id.progressBar);
        textViewFullName = findViewById(R.id.textView_full_name);
        textViewStreet = findViewById(R.id.textView_street);
        textViewCity = findViewById(R.id.textView_city);
        textViewCounty = findViewById(R.id.textView_country);
        textViewZip = findViewById(R.id.textView_zip);
        textViewMobile = findViewById(R.id.textView_phone);
        listView = findViewById(R.id.listView1);
        buttonNewCard = findViewById(R.id.button_CreditCard);
        RadiobuttonCreditCard = findViewById(R.id.radio_credit_card);
        RadiobuttonPaypal = findViewById(R.id.radio_paypal);
        RadiobuttonBit = findViewById(R.id.radio_bit);
        editTextPaypal = findViewById(R.id.editTextPaypal);
        editTextBit = findViewById(R.id.editTextBit);

        books = new ArrayList<>();





        buttonNewCard.setEnabled(false);
        editTextBit.setEnabled(false);
        editTextPaypal.setEnabled(false);
        ColorDrawable gray = new ColorDrawable(Color.parseColor("#607D8B"));

        RadiobuttonCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonNewCard.setEnabled(true);
                editTextBit.setEnabled(false);
                editTextPaypal.setEnabled(false);
                editTextPaypal.setBackground(gray);
                editTextBit.setBackground(gray);

            }
        });
        RadiobuttonPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonNewCard.setEnabled(false);
                editTextPaypal.setEnabled(true);
                editTextBit.setEnabled(false);
                editTextBit.setBackground(gray);
                editTextPaypal.setBackgroundResource(R.drawable.border);
            }
        });
        RadiobuttonBit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonNewCard.setEnabled(false);
                editTextPaypal.setEnabled(false);
                editTextBit.setEnabled(true);
                editTextPaypal.setBackground(gray);
                editTextBit.setBackgroundResource(R.drawable.border);
            }
        });
        Bundle bundle = getIntent().getExtras();
        if(getIntent() != null ){
             books = (ArrayList<BookItem>) bundle.getSerializable("BookItem");
             listAdapter = new ListAdapter(getApplicationContext(),books);
             listView.setAdapter(listAdapter);

        }

        buttonNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this,CreditCardActivity.class);
                extras = new Bundle ();
                extras.putSerializable("Books",books);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        progressBar.setVisibility(View.VISIBLE);
        showUserDetails(firebaseUser);
        showAddress(firebaseUser);
        progressBar.setVisibility(View.GONE);
    }
        private void showUserDetails(FirebaseUser firebaseUser){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    UserClass ReadUserDetails = snapshot.getValue(UserClass.class);
                    if(ReadUserDetails != null){
                        fullName = ReadUserDetails.getFull_name();
                        Phone = ReadUserDetails.getPhone();
                        textViewFullName.setText(fullName);
                        textViewMobile.setText(Phone);
                    }else{
                        progressBar.setVisibility(View.GONE);
                    }
                }else{
                    databaseReference.child("Authors").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                UserClass ReadUserDetails = snapshot.getValue(UserClass.class);
                                if (ReadUserDetails != null) {
                                    fullName = ReadUserDetails.getFull_name();
                                    Phone = ReadUserDetails.getPhone();
                                    textViewFullName.setText(fullName);
                                    textViewMobile.setText(Phone);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                }
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
                Toast.makeText(CheckoutActivity.this,"Something went wrong ! ",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void  showAddress(FirebaseUser firebaseUser ) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    databaseReference.child(firebaseUser.getUid()).child("Address").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            AddressClass UserAddress = snapshot.getValue(AddressClass.class);
                            if (UserAddress != null) {
                                Street = UserAddress.getStreet();
                                City = UserAddress.getCity();
                                County = UserAddress.getCounty();
                                Zip = UserAddress.getPostal_Code();
                                textViewStreet.setText(Street);
                                textViewCity.setText(City);
                                textViewCounty.setText(County);
                                textViewZip.setText(Zip);
                            } else {
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(CheckoutActivity.this,"Something went wrong ! ",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }else{
                    databaseReference.child("Authors").child(firebaseUser.getUid()).child("Address").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                AddressClass UserAddress = snapshot.getValue(AddressClass.class);
                                if (UserAddress != null) {
                                    Street = UserAddress.getStreet();
                                    City = UserAddress.getCity();
                                    County = UserAddress.getCounty();
                                    Zip = UserAddress.getPostal_Code();
                                    textViewStreet.setText(Street);
                                    textViewCity.setText(City);
                                    textViewCounty.setText(County);
                                    textViewZip.setText(Zip);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                }
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
        if(id == R.id.menu_refresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if (id == R.id.menu_profile){
            Intent intent = new Intent(CheckoutActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_cart){
            Intent intent = new Intent(CheckoutActivity.this,MyCartActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_my_list){
            Intent intent = new Intent(CheckoutActivity.this,MyListActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_settings){
            Intent intent = new Intent(CheckoutActivity.this,SettingsActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_home){
            Intent intent = new Intent(CheckoutActivity.this,NewHomeActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_log_out){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(CheckoutActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CheckoutActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
