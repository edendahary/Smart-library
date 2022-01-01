package com.example.edenproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;

public class UpdateProfileActivity extends AppCompatActivity {
    private TextView textView_fullname,textViewFullName,textViewBday,textViewGender, textViewPhone_number;
    private String fullname;
    private FirebaseAuth authProfile;
    private DatePickerDialog picker;
    private ProgressBar progressBar;
    private Button add_Profile;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#c1461d"));
        getSupportActionBar().setBackgroundDrawable(cd);
        getSupportActionBar().setTitle("Edit Profile");


        textViewFullName = findViewById(R.id.textView_show_full_name);
        textView_fullname = findViewById(R.id.editText_fullname);
        textViewBday = findViewById(R.id.editText_Bday);
        textViewGender = findViewById(R.id.editText_Gender);
        textViewPhone_number = findViewById(R.id.editText_PhoneNumber);
        add_Profile = findViewById(R.id.button_add_profile);

        progressBar = findViewById(R.id.progressBar);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();



        if(firebaseUser == null){
            Toast.makeText(UpdateProfileActivity.this,"Something went wrong ! User's details are not available",Toast.LENGTH_LONG).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        textViewBday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(UpdateProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textViewBday.setText(dayOfMonth + "/"+(month+1)+"/"+year);
                    }
                },year,month,day);
                picker.show();
            }
        });



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
                Toast.makeText(UpdateProfileActivity.this,"Something went wrong ! ",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });

        add_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_full_name = textView_fullname.getText().toString();
                String userEmail = firebaseUser.getEmail();
                String text_Bday = textViewBday.getText().toString();
                String text_Gender = textViewGender.getText().toString();
                String text_Phone_number = textViewPhone_number.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                UserClass UserClass = new UserClass(text_full_name,userEmail,text_Bday, text_Phone_number,text_Gender);

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");


                referenceProfile.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            if (!TextUtils.isEmpty(text_full_name)) {
                                referenceProfile.child(firebaseUser.getUid()).child("full_name").setValue(text_full_name);
                            }
                            if (!TextUtils.isEmpty(text_Bday)) {
                                referenceProfile.child(firebaseUser.getUid()).child("dob").setValue(text_Bday);
                            }
                            if (!TextUtils.isEmpty(text_Gender)) {
                                referenceProfile.child(firebaseUser.getUid()).child("gender").setValue(text_Gender);
                            }
                            if (!TextUtils.isEmpty(text_Phone_number)) {
                                referenceProfile.child(firebaseUser.getUid()).child("phone").setValue(text_Phone_number);
                            }
                            startActivity(new Intent(UpdateProfileActivity.this,ProfileActivity.class));
                            finish();
                        }else {
                            if (!TextUtils.isEmpty(text_full_name)) {
                                referenceProfile.child("Authors").child(firebaseUser.getUid()).child("full_name").setValue(text_full_name);
                            }
                            if (!TextUtils.isEmpty(text_Bday)) {
                                referenceProfile.child("Authors").child(firebaseUser.getUid()).child("dob").setValue(text_Bday);
                            }
                            if (!TextUtils.isEmpty(text_Gender)) {
                                referenceProfile.child("Authors").child(firebaseUser.getUid()).child("gender").setValue(text_Gender);
                            }
                            if (!TextUtils.isEmpty(text_Phone_number)) {
                                referenceProfile.child("Authors").child(firebaseUser.getUid()).child("phone").setValue(text_Phone_number);
                            }
                            startActivity(new Intent(UpdateProfileActivity.this,ProfileActivity.class));
                            finish();
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
        } else if (id == R.id.menu_history_list){
            startActivity(new Intent(UpdateProfileActivity.this, HistoryActivity.class));
            finish();
        }else if(id == R.id.menu_my_list){
            Intent intent = new Intent(UpdateProfileActivity.this,MyListActivity.class);
            startActivity(intent);
            finish();
        }else if(id == R.id.menu_new_book){
            Intent intent = new Intent(UpdateProfileActivity.this,AddBooksActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_profile){
            Intent intent = new Intent(UpdateProfileActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_settings){
            Intent intent = new Intent(UpdateProfileActivity.this,SettingsActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_cart){
            Intent intent = new Intent(UpdateProfileActivity.this,MyCartActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.menu_log_out){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(UpdateProfileActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateProfileActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_home){
            startActivity(new Intent(UpdateProfileActivity.this, NewHomeActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}