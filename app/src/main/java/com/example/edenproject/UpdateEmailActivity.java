package com.example.edenproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateEmailActivity extends AppCompatActivity {
    private TextView textViewAuthenticated;
    private EditText editTextCurrPwd,editTextNewEmail;
    private Button buttonChangeEmail,buttonReAuthenticated;
    private ProgressBar progressBar;
    private String userPwd;


    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        getSupportActionBar().setTitle("Change Your Email");
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#56bffa"));
        getSupportActionBar().setBackgroundDrawable(cd);

        textViewAuthenticated = findViewById(R.id.textView_change_email);

        editTextCurrPwd = findViewById(R.id.editText_curr_pwd);
        editTextNewEmail = findViewById(R.id.editText_new_email);

        buttonChangeEmail = findViewById(R.id.button_change_email);
        buttonReAuthenticated = findViewById(R.id.button_authenticate_pwd);

        progressBar = findViewById(R.id.progressBar);


        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser.equals("")){
            Toast.makeText(UpdateEmailActivity.this, "Something went wrong." +
                    "User Details are not available at the moment", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateEmailActivity.this,LoginActivity.class);
        }else{
            reAuthenticateUser(firebaseUser);
        }

    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonReAuthenticated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwd = editTextCurrPwd.getText().toString();
                if(TextUtils.isEmpty(userPwd)){
                    Toast.makeText(UpdateEmailActivity.this, "Password is needed", Toast.LENGTH_SHORT).show();
                    editTextCurrPwd.setText("Please enter your current password to authenticate");
                    editTextCurrPwd.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),userPwd);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                editTextCurrPwd.setEnabled(false);

                                buttonReAuthenticated.setEnabled(false);
                                buttonChangeEmail.setEnabled(true);

                                textViewAuthenticated.setText("Yor are authenticated/verified" + "You can change your email now");
                                Toast.makeText(UpdateEmailActivity.this, "Password has been verified." + "You can change your email now." +
                                        " Be careful this action is irreversible", Toast.LENGTH_SHORT).show();

                                buttonReAuthenticated.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmailActivity.this, R.color.design_default_color_primary_dark));

                                buttonChangeEmail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showAlertDialog();
                                    }
                                });
                            }else{
                                try{
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(UpdateEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEmailActivity.this);
        builder.setTitle("Change Email");
        builder.setMessage("Do you really want to change your email!");
        builder.setPositiveButton("Continue ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChangeUserPwd(firebaseUser);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(UpdateEmailActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.design_default_color_primary_dark));
            }
        });

        alertDialog.show();

    }

    private void ChangeUserPwd(FirebaseUser firebaseUser) {
        firebaseUser.updateEmail(editTextNewEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    authProfile.signOut();
                    Toast.makeText(UpdateEmailActivity.this, "Your email has been changed!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateEmailActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    try {
                        task.getException();
                    }catch (Exception e){
                        Toast.makeText(UpdateEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
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
        if(id == R.id.menu_refresh ){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if (id == R.id.menu_profile){
            Intent intent = new Intent(UpdateEmailActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_my_list){
            Intent intent = new Intent(UpdateEmailActivity.this,MyListActivity.class);
            startActivity(intent);
            finish();
        }else if(id == R.id.menu_home){
            Intent intent = new Intent(UpdateEmailActivity.this,NewHomeActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_settings){
            Intent intent = new Intent(UpdateEmailActivity.this,SettingsActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_log_out){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(UpdateEmailActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateEmailActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}