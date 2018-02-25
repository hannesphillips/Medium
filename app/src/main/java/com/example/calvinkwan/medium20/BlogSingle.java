package com.example.calvinkwan.medium20;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class BlogSingle extends AppCompatActivity {
    private String postKey = null;
    private StorageReference storage;
    private DatabaseReference mDatabase;
    private DatabaseReference bookmarks;
    private DatabaseReference users;

    private ImageView singleImage;
    private TextView singleTitle;
    private TextView singleDesc;
    private TextView singleName;

    private ImageButton bookmarkButton;
    private Uri imageUri = null;

    private String post_title;
    private String post_desc;
    private String post_image;
    private String post_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single);
        // setContentView(R.layout.blog_row);

        storage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        users = FirebaseDatabase.getInstance().getReference().child("Users");
        postKey = getIntent().getExtras().getString("blog_id");

        singleImage = findViewById(R.id.imageSingle);
        singleTitle = findViewById(R.id.postTitle);
        singleDesc = findViewById(R.id.postDescription);
        singleName = findViewById(R.id.postUser);
        bookmarkButton = findViewById(R.id.bookmark);

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BlogSingle.this, "Bookmarked", Toast.LENGTH_LONG).show();
                bookmark();
            }
        });

        mDatabase.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post_title = (String) dataSnapshot.child("title").getValue();
                post_desc = (String) dataSnapshot.child("desc").getValue();
                post_image = (String) dataSnapshot.child("image").getValue();
                post_name = (String) dataSnapshot.child("name").getValue();

                singleTitle.setText(post_title);
                singleDesc.setText(post_desc);
                singleName.setText(post_name);
                Picasso.with(BlogSingle.this).load(post_image).into(singleImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void bookmark() {
//        StorageReference filePath = storage.child("Blog_Images").child(imageUri.getLastPathSegment());
//
//        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
//        {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
//            {
//                Uri downloadUri = taskSnapshot.getDownloadUrl();
//                final DatabaseReference newPost = mDatabase.push();
//                newPost.child("title").setValue(post_title);
//                newPost.child("desc").setValue(post_desc);
//                newPost.child("image").setValue(post_image);
//
//                String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                users = FirebaseDatabase.getInstance().getReference().child("Users");
//                users.child(user_key).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        final String u  = (String) dataSnapshot.child("name").getValue();
//                        newPost.child("name").setValue(u);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//
//                startActivity(new Intent(PostActivity.this, MainActivity.class));       //return to timeline
//            }
//        });

//        final DatabaseReference newPost = mDatabase.push();
//        newPost.child("bookmarks");
////        newPost.child("title").setValue(post_title);
////        newPost.child("desc").setValue(post_desc);
////        newPost.child("image").setValue(post_image);
//
//        String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        bookmarks = FirebaseDatabase.getInstance().getReference().child("Users");
//        bookmarks.child(user_key).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        final DatabaseReference newPost = users.push();
//        newPost.child("bookmarks");


        // NOTE: DOESN'T CHECK FOR DUPLICATE BOOKMARKS

        // String post_key = mDatabase.child
        String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference addBookmark = users.child(user_key);
        DatabaseReference here = addBookmark.child("Bookmarks");
        final DatabaseReference newBookmark = here.push();
        // newBookmark.setValue(postKey);
        newBookmark.child("title").setValue(post_title);
        newBookmark.child("desc").setValue(post_desc);
        newBookmark.child("image").setValue(post_image);

    }
}
