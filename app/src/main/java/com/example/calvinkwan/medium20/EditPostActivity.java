package com.example.calvinkwan.medium20;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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


public class EditPostActivity extends AppCompatActivity {
    private String postKey = null;

    private DatabaseReference mDatabase;

    private TextView editTitle;
    private TextView editDesc;

    private Button cancelButton;
    private Button editButton;

    private ImageView singleImage;

    private String post_title;
    private String post_desc;
    private String post_image;
    private String userKey;

    private boolean valid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        postKey = getIntent().getExtras().getString("blog_id");

        singleImage = findViewById(R.id.imageSingle);
        editTitle = findViewById(R.id.postTitle);
        editDesc = findViewById(R.id.postDescription);

        cancelButton = findViewById(R.id.cancel_edit);
        editButton = findViewById(R.id.submit_edit);

        mDatabase.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post_title = (String) dataSnapshot.child("title").getValue();
                post_desc = (String) dataSnapshot.child("desc").getValue();
                post_image = (String) dataSnapshot.child("image").getValue();
                userKey = (String) dataSnapshot.child("userKey").getValue();
                editTitle.setText(post_title);
                editDesc.setText(post_desc);
                Picasso.with(EditPostActivity.this).load(post_image).into(singleImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSingle = new Intent(EditPostActivity.this, BlogSingle.class);
                toSingle.putExtra("blog_id", postKey);
                startActivity(toSingle);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid = updatePost();
                if(valid) {
                    Intent toSingle = new Intent(EditPostActivity.this, BlogSingle.class);
                    toSingle.putExtra("blog_id", postKey);
                    startActivity(toSingle);
                }
            }
        });
    }

    private boolean updatePost() {

        final String titleText = editTitle.getText().toString().trim();
        final String descText = editDesc.getText().toString().trim();

        if (!TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(descText)) {

            mDatabase.child(postKey).child("title").setValue(titleText);
            mDatabase.child(postKey).child("desc").setValue(descText);
            return true;
        }
        else if(TextUtils.isEmpty(titleText)) {
            Toast.makeText(getApplicationContext(), "Can not have an empty Title..", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(TextUtils.isEmpty(descText)) {
            Toast.makeText(getApplicationContext(), "Can not have an empty Description..", Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return false;
    }
}
