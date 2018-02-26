package com.example.calvinkwan.medium20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BlogSingle extends AppCompatActivity {
    private String postKey = null;
    private DatabaseReference mDatabase;

    private ImageView singleImage;
    private TextView singleTitle;
    private TextView singleDesc;
    private TextView singleName;
    private TextView singleCateg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
         postKey = getIntent().getExtras().getString("blog_id");

        singleImage = findViewById(R.id.imageSingle);
        singleTitle = findViewById(R.id.postTitle);
        singleDesc = findViewById(R.id.postDescription);
        singleName = findViewById(R.id.postUser);
        //singleCateg = findViewById(R.id.postCateg);

        mDatabase.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                String post_name = (String) dataSnapshot.child("name").getValue();
                String post_categ = (String) dataSnapshot.child("categ").getValue();

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
}
