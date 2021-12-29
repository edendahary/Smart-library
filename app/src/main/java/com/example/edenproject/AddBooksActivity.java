package com.example.edenproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddBooksActivity extends AppCompatActivity {
    private FirebaseAuth authProfile;
    private ImageButton imageButtonBookAddImage;
    private StorageReference storageReference;
    private Uri uri;
    private DatabaseReference databaseReference;
    private String image;
    private ProgressBar progressBar;
    private FirebaseUser firebaseUser;
    private Button buttonAddBook;
    private EditText editTextAuthorName,
            editTextPages, editTextBookName, editTextCategoryName, EditTextPublicationDate, editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#c1461d"));
        getSupportActionBar().setBackgroundDrawable(cd);

        imageButtonBookAddImage = findViewById(R.id.imageButton_add_image_book);
        buttonAddBook = findViewById(R.id.button_add_book);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();




        editTextAuthorName = findViewById(R.id.editText_author_name);
        editTextPages = findViewById(R.id.editText_pages);
        editTextBookName = findViewById(R.id.editText_book_name);
        editTextCategoryName = findViewById(R.id.editText_category_book);
        EditTextPublicationDate = findViewById(R.id.editText_publication);
        editTextDescription = findViewById(R.id.editText_description);

        storageReference = FirebaseStorage.getInstance().getReference("Books");


        imageButtonBookAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBooksActivity.this, UploadBookPicActivity.class);
                startActivity(intent);
                finish();

            }
        });
        //--- Get the uri ---\\

        if(getIntent() != null) {
            Intent intent1 = getIntent();
            image = intent1.getStringExtra("uri");
            Glide.with(AddBooksActivity.this).load(image).into(imageButtonBookAddImage);
        }



        buttonAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textBookName = editTextBookName.getText().toString();
                String textAuthorName = editTextAuthorName.getText().toString();
                String textCategory = editTextCategoryName.getText().toString();
                String textPublicationDate = EditTextPublicationDate.getText().toString();
                String textDescription = editTextDescription.getText().toString();
                if (TextUtils.isEmpty(textBookName)) {
                    Toast.makeText(AddBooksActivity.this, "Please enter book name", Toast.LENGTH_SHORT).show();
                    editTextBookName.setError("Full Book Name is required");
                    editTextBookName.requestFocus();
                } else if((textAuthorName).matches(".*\\d.*")){
                    Toast.makeText(AddBooksActivity.this, "Please enter author name with no numbers", Toast.LENGTH_SHORT).show();
                    editTextAuthorName.setError("Author Book Name is required(No Digits)");
                    editTextAuthorName.requestFocus();
                }else if (TextUtils.isEmpty(textAuthorName)) {
                    Toast.makeText(AddBooksActivity.this, "Please enter author name", Toast.LENGTH_SHORT).show();
                    editTextAuthorName.setError("Author Name is required");
                    editTextAuthorName.requestFocus();
                } else if (TextUtils.isEmpty(textPublicationDate)) {
                    Toast.makeText(AddBooksActivity.this, "Please enter publication date", Toast.LENGTH_SHORT).show();
                    EditTextPublicationDate.setError("publication date is required");
                    EditTextPublicationDate.requestFocus();
                } else if (TextUtils.isEmpty(textDescription)) {
                    Toast.makeText(AddBooksActivity.this, "Please enter description", Toast.LENGTH_SHORT).show();
                    editTextDescription.setError("Description is required");
                    editTextDescription.requestFocus();
                } else {
                    String pages = editTextPages.getText().toString();
                    int page = Integer.parseInt(pages);
                    progressBar.setVisibility(View.VISIBLE);
                    BookItem book = new BookItem(textBookName, textAuthorName, textCategory, textPublicationDate, textDescription, page,image);

                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
                    DatabaseReference referenceBooks = FirebaseDatabase.getInstance().getReference("AllBooks");
                    referenceBooks.child(book.getName()).setValue(book);
                    referenceProfile.child("Authors").child(firebaseUser.getUid()).child("Books").child(book.getName()).setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(AddBooksActivity.this, MyListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
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
        if(id == R.id.menu_refresh || id == R.id.menu_new_book){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if (id == R.id.menu_cart){
            Intent intent = new Intent(AddBooksActivity.this,MyCartActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_profile){
            Intent intent = new Intent(AddBooksActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_my_list){
            Intent intent = new Intent(AddBooksActivity.this,MyListActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_settings){
            Intent intent = new Intent(AddBooksActivity.this,SettingsActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_home){
            Intent intent = new Intent(AddBooksActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_log_out){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(AddBooksActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddBooksActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}








