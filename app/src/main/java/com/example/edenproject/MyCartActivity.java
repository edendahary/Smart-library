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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyCartActivity extends AppCompatActivity implements customAdapter.CheckBoxCheckedListener {
    private ListView listView;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    private ArrayList<BookItem> books;
    private ArrayList<BookItem> check_book;
    private Button ProceedToCheckout;
    private customAdapter customAdapter;
    private CheckBox checkBox;
    private BookItem bookItem;
    private DatabaseReference referenceBooks;
    private DatabaseReference databaseReference;
    private Bundle extras;
    private DatabaseReference referenceProfile;




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
        books =new ArrayList<BookItem>();
        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();


        progressBar.setVisibility(View.VISIBLE);

        showUserBooks(firebaseUser);

        progressBar.setVisibility(View.GONE);



        //---Pass the books  --\\
        ProceedToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCartActivity.this,CheckoutActivity.class);
                extras = new Bundle ();
                extras.putSerializable("BookItem",check_book);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showUserBooks(FirebaseUser firebaseUser) {
        //--- Getting the books that the user want ---\\
        referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
        referenceProfile.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    referenceProfile.child(firebaseUser.getUid()).child("Cart").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    BookItem currBook = dataSnapshot.getValue(BookItem.class);
                                    referenceBooks = FirebaseDatabase.getInstance().getReference("AllBooks");
                                    referenceBooks.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                for(DataSnapshot book : snapshot.getChildren()){
                                                    BookItem b = book.getValue(BookItem.class);
                                                    if(b.getName().equals(currBook.getName())){
                                                        books.add(currBook);
                                                        break;
                                                    }
                                                }

                                            }
                                            customAdapter = new customAdapter(MyCartActivity.this,books);
                                            listView.setAdapter(customAdapter);
                                            customAdapter.setCheckedListener(MyCartActivity.this);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

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
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    BookItem currBook = dataSnapshot.getValue(BookItem.class);
                                    referenceBooks = FirebaseDatabase.getInstance().getReference("AllBooks");
                                    referenceBooks.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                for(DataSnapshot book : snapshot.getChildren()){
                                                    BookItem b = book.getValue(BookItem.class);
                                                    if(b.getName().equals(currBook.getName())){
                                                        books.add(currBook);
                                                        break;
                                                    }
                                                }

                                            }
                                            customAdapter = new customAdapter(MyCartActivity.this,books);
                                            listView.setAdapter(customAdapter);
                                            customAdapter.setCheckedListener(MyCartActivity.this);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
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


    // Check if the current book is mark \\
    @Override
    public void getCheckboxCheckedListener(int position) {
        if(check_book != null && check_book.contains(books.get(position))){
            check_book.remove(books.get(position));
        }else {
            check_book.add(books.get(position));
        }

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
        if(id == R.id.menu_refresh || id == R.id.menu_cart){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if (id == R.id.menu_new_book){
            Intent intent = new Intent(MyCartActivity.this,AddBooksActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_history_list) {
            Intent intent = new Intent(MyCartActivity.this, HistoryActivity.class);
            startActivity(intent);
            finish();
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
            Intent intent = new Intent(MyCartActivity.this,NewHomeActivity.class);
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