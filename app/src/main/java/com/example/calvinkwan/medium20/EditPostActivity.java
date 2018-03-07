package com.example.calvinkwan.medium20;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class EditPostActivity extends AppCompatActivity {
    private String postKey = null;

    private StorageReference storage;
    private DatabaseReference mDatabase;
    private DatabaseReference users;

    private ImageView singleImage;
    private TextView singleTitle;
    private TextView singleDesc;
    private TextView singleName;

    private Uri imageUri = null;

    private String post_title;
    private String post_desc;
    private String post_image;
    private String post_name;

    private int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        storage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
//        users = FirebaseDatabase.getInstance().getReference().child("Users");
        postKey = getIntent().getExtras().getString("blog_id");

        singleImage = findViewById(R.id.imageSingle);
        singleTitle = findViewById(R.id.postTitle);
        singleDesc = findViewById(R.id.postDescription);
        singleName = findViewById(R.id.postUser);


        mDatabase.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post_title = (String) dataSnapshot.child("title").getValue();
                post_desc = (String) dataSnapshot.child("desc").getValue();
                post_image = (String) dataSnapshot.child("image").getValue();

                singleTitle.setText(post_title);
                singleDesc.setText(post_desc);
                Picasso.with(EditPostActivity.this).load(post_image).into(singleImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
