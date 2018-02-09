package com.example.calvinkwan.medium20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class PostActivity extends AppCompatActivity {

    private ImageButton selectImage;
    private EditText postTitle;
    private EditText postDescription;
    private Button submitButton;
    private StorageReference storage;
    private DatabaseReference database;
    private ProgressDialog progress;

    private Uri imageUri = null;

    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference().child("Blog");

        selectImage = (ImageButton) findViewById(R.id.imageButton);
        postTitle = (EditText) findViewById(R.id.postTitle);
        postDescription = (EditText) findViewById(R.id.postDescription);
        submitButton = (Button) findViewById(R.id.submitPost);
        progress = new ProgressDialog(this);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_REQUEST);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPost();
            }
        });
    }



    private void sendPost() {
            progress.setMessage("Posting to blog...");
            progress.show();
            String titleText = postTitle.getText().toString().trim();
            String descText = postDescription.getText().toString().trim();

            if (!TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(descText) && imageUri != null) {
                StorageReference filePath = storage.child("Blog_Images").child(imageUri.getLastPathSegment());
                filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        DatabaseReference newPost = database.push();
                        progress.dismiss();
                    }
                });
            }
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
                imageUri = data.getData();
                selectImage.setImageURI(imageUri);
                ;
            }
        }
    }

