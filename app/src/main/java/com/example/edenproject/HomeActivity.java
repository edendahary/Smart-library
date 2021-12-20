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
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_TEXT = "EXTRA_TEXT";
    public static final String EXTRA_IMAGE = "EXTRA_IMAGE";
    public static final String EXTRA_IMAGES = "EXTRA_AIMAGE";
    public static final String EXTRA_BOOKS = "EXTRA_ARRAY_BOOK";
    private ImageButton GetImageView;
    private Bundle extras;
    private Intent intent;

    private ImageButton buttonImage_last_wish,buttonImage_sword_of_destiny,buttonImage_blood_of_elves,buttonImage_the_tower_Of_the_swallow;
    private TextView textViewLastWish,textViewSwordOfDestiny,textViewBloodOfElves,textViewTheTowerOfTheSwallow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Home");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#c1461d"));
        getSupportActionBar().setBackgroundDrawable(cd);

        //--- TextView ---\\
        textViewLastWish = findViewById(R.id.textview_last_wish);
        textViewSwordOfDestiny = findViewById(R.id.textView_sword_of_destiny);
        textViewBloodOfElves = findViewById(R.id.textView_blood_of_elves);
        textViewTheTowerOfTheSwallow = findViewById(R.id.textView_the_tower_of_the_swallow);


        //--- Buttons ---\\
        buttonImage_last_wish = findViewById(R.id.imageButton_the_last_wish);
        buttonImage_sword_of_destiny = findViewById(R.id.imageButton_sword_of_destiny);
        buttonImage_blood_of_elves = findViewById(R.id.imageButton_blood_of_elves);
        buttonImage_the_tower_Of_the_swallow = findViewById(R.id.imageButton_the_tower_of_the_swallow);

        buttonImage_last_wish.setOnClickListener(this);
        buttonImage_sword_of_destiny.setOnClickListener(this);
        buttonImage_blood_of_elves.setOnClickListener(this);
        buttonImage_the_tower_Of_the_swallow.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageButton_the_last_wish:

                //--- Get the drawable Image---\\
                GetImageView = findViewById (R.id.imageButton_the_last_wish);
                GetImageView.buildDrawingCache ();

                extras = new Bundle ();
                extras.putParcelable (EXTRA_IMAGE, GetImageView.getDrawingCache ());
                //-------------------------------\\


                intent = new Intent(HomeActivity.this,ChooseBookActivity.class);
                intent.putExtra(EXTRA_TEXT,textViewLastWish.getText().toString());
                intent.putExtras (extras);
                startActivity(intent);
                finish();
                break;

            case R.id.imageButton_sword_of_destiny:
                //--- Get the drawable Image---\\
                GetImageView = findViewById (R.id.imageButton_sword_of_destiny);
                GetImageView.buildDrawingCache ();

                extras = new Bundle ();
                extras.putParcelable (EXTRA_IMAGE, GetImageView.getDrawingCache ());
                //-------------------------------\\


                intent = new Intent(HomeActivity.this,ChooseBookActivity.class);
                intent.putExtra(EXTRA_TEXT,textViewSwordOfDestiny.getText().toString());
                intent.putExtras (extras);
                startActivity(intent);
                finish();
                break;
            case R.id.imageButton_blood_of_elves:
                //--- Get the drawable Image---\\
                GetImageView = findViewById (R.id.imageButton_blood_of_elves);
                GetImageView.buildDrawingCache ();

                extras = new Bundle ();
                extras.putParcelable (EXTRA_IMAGE, GetImageView.getDrawingCache ());
                //-------------------------------\\


                intent = new Intent(HomeActivity.this,ChooseBookActivity.class);
                intent.putExtra(EXTRA_TEXT,textViewBloodOfElves.getText().toString());
                intent.putExtras (extras);
                startActivity(intent);
                finish();
                break;
            case R.id.imageButton_the_tower_of_the_swallow:
                //--- Get the drawable Image---\\
                GetImageView = findViewById (R.id.imageButton_the_tower_of_the_swallow);
                GetImageView.buildDrawingCache ();

                extras = new Bundle ();
                extras.putParcelable (EXTRA_IMAGE, GetImageView.getDrawingCache ());
                //-------------------------------\\


                intent = new Intent(HomeActivity.this,ChooseBookActivity.class);
                intent.putExtra(EXTRA_TEXT,textViewTheTowerOfTheSwallow.getText().toString());
                intent.putExtras (extras);
                startActivity(intent);
                finish();
                break;

        }
    }












    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_refresh || id == R.id.menu_home){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if (id == R.id.menu_profile){
            Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_my_list){
            Intent intent = new Intent(HomeActivity.this,MyListActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_settings){
            Intent intent = new Intent(HomeActivity.this,SettingsActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_cart){
            Intent intent = new Intent(HomeActivity.this,MyCartActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_log_out){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(HomeActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}