package com.example.calvinkwan.medium20;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private DatabaseReference likes;


    private ImageView singleImage;
    private TextView singleTitle;
    private TextView singleDesc;
    private TextView singleName;
    private TextView singleCateg;

    private ImageButton bookmarkButton;
    private ImageButton likeButton;

    private Uri imageUri = null;

    private String post_title;
    private String post_desc;
    private String post_image;
    private String post_name;

    private int flag;
    private boolean bkbut = false;

    private String bPostKey;

    boolean mProcessLike;

    String curr_user = "";
    String poster = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single);
        // setContentView(R.layout.blog_row);

        storage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        users = FirebaseDatabase.getInstance().getReference().child("Users");
        likes = FirebaseDatabase.getInstance().getReference().child("Likes");
        postKey = getIntent().getExtras().getString("blog_id");

        flag = 0;
        flag = getIntent().getExtras().getInt("flag");

        // change mdatabase
        if (flag == 1) {
            mDatabase = users;
            String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase = mDatabase.child(user_key);
            mDatabase = mDatabase.child("Bookmarks");
        }

        // Toast.makeText(BlogSingle.this, postKey, Toast.LENGTH_LONG).show();

        singleImage = findViewById(R.id.imageSingle);
        singleTitle = findViewById(R.id.postTitle);
        singleDesc = findViewById(R.id.postDescription);
        singleName = findViewById(R.id.postUser);
        bookmarkButton = findViewById(R.id.bookmark);
        likeButton = findViewById(R.id.likebtn);

        //FOR LIKES:::
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BlogSingle.this, "Liked", Toast.LENGTH_LONG).show();
                String userkey = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference addLike = users.child(userkey);
                DatabaseReference here = addLike.child("Likes");
                final DatabaseReference newLike = here.push();
                newLike.child("title").setValue(post_title);
            }
        });

//        mDatabase.child(postKey).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                post_name = (String) dataSnapshot.child("name").getValue();
//
//                singleName.setText(post_name);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        //FOR BOOKMARKS:
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(BlogSingle.this, "Bookmarked", Toast.LENGTH_LONG).show();
                bookmark();
            }
        });
        //singleCateg = findViewById(R.id.postCateg);

        mDatabase.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post_title = (String) dataSnapshot.child("title").getValue();
                post_desc = (String) dataSnapshot.child("desc").getValue();
                post_image = (String) dataSnapshot.child("image").getValue();
                post_name = (String) dataSnapshot.child("name").getValue();
//                String post_title = (String) dataSnapshot.child("title").getValue();
//                String post_desc = (String) dataSnapshot.child("desc").getValue();
//                String post_image = (String) dataSnapshot.child("image").getValue();
//                String post_name = (String) dataSnapshot.child("name").getValue();
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

    private void bookmark() {


        // NOTE: DOESN'T CHECK FOR DUPLICATE BOOKMARKS

        // String post_key = mDatabase.child
        String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference addBookmark = users.child(user_key);
        DatabaseReference here = addBookmark.child("Bookmarks");

        // if postkey in bookmarks, unbookmark

        final DatabaseReference newBookmark = here.push();

        bkbut = true;

        here.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (bkbut) {
                    if (dataSnapshot.hasChild(postKey)) {
                        delBookmark();
                        bkbut = false;
                    } else {
                        newBookmark.child("title").setValue(post_title);
                        newBookmark.child("desc").setValue(post_desc);
                        newBookmark.child("image").setValue(post_image);
                        newBookmark.child("postkey").setValue(postKey);
                        bkbut = false;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    void delBookmark() {
        mDatabase = users;
        String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = mDatabase.child(user_key);
        mDatabase = mDatabase.child("Bookmarks");
        mDatabase.child(postKey).removeValue();
        finish();
        // return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.blog_single_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // get name of post
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        postKey = getIntent().getExtras().getString("blog_id");
        assert postKey != null;
        mDatabase.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s = (String) dataSnapshot.child("name").getValue();
                if(s != null) poster = s;
            }
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // get name of user

        String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase.child(user_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String u  = (String) dataSnapshot.child("name").getValue();
                if(u != null) curr_user = u;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        boolean q = curr_user.equals(poster);
        if (q)
        {
            // Add delete option
            menu.findItem(R.id.del_post).setVisible(true);
        }
        else
        {
            menu.findItem(R.id.del_post).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.del_post)
        {
            delPost();
            Intent toMain = new Intent(this, MainActivity.class);
            startActivity(toMain);
        }

        return super.onOptionsItemSelected(item);
    }

    void delPost() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        postKey = getIntent().getExtras().getString("blog_id");
        if (postKey != null) mDatabase.child(postKey).removeValue();

        return;
    }

}
