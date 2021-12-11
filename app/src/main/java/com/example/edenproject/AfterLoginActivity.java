package com.example.edenproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AfterLoginActivity extends AppCompatActivity {
    private Button logout;
    private Button lib;
    private Button profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_login_activity);

        logout = findViewById(R.id.btnLogOut);
        lib = findViewById(R.id.BtnLibrary);
        profile = findViewById(R.id.BtnProfile);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(AfterLoginActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AfterLoginActivity.this,LoginActivity.class));
                finish();
            }
        });

        lib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AfterLoginActivity.this, HomeActivity.class));
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AfterLoginActivity.this, ProfileActivity.class));
                finish();
            }
        });



    }
}