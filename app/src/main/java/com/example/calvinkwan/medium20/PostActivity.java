package com.example.calvinkwan.medium20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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


public class PostActivity extends AppCompatActivity {

    private ImageButton selectImage;
    private EditText postTitle;
    private EditText postDescription;
    private EditText postName;
    private Spinner postCateg;

    private Button submitButton;
    private StorageReference storage;
    private DatabaseReference database;
    private DatabaseReference users;
    private DatabaseReference likes;
    private ProgressDialog progress;
    private DatabaseReference mtemp;
    private String postKey = null;
    private String user_key = null;
    private String key = null;
    private Uri imageUri = null;

    private DatabaseReference Art;
    private DatabaseReference Food;
    private DatabaseReference Sports;
    private DatabaseReference Photography;
    private DatabaseReference Posts;

    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference().child("Blog");

        String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        users = FirebaseDatabase.getInstance().getReference().child("Users");


        // test category
        Art = FirebaseDatabase.getInstance().getReference().child("Art");
        Food = FirebaseDatabase.getInstance().getReference().child("Food");
        Sports = FirebaseDatabase.getInstance().getReference().child("Sports");
        Photography = FirebaseDatabase.getInstance().getReference().child("Photography");
        Posts = FirebaseDatabase.getInstance().getReference().child("Users");
        Posts = Posts.child(user_key).child("Personal Posts");


        selectImage = findViewById(R.id.likebutton);
        postTitle = findViewById(R.id.postTitle);
        postDescription = findViewById(R.id.postDescription);
        postCateg = findViewById(R.id.spinner1);


        submitButton = findViewById(R.id.submitPost);
        progress = new ProgressDialog(this);

        // <----------------------- Category drop down list --------------------------------------->
        final Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(PostActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Category));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        // <----------------------- End category drop down list ----------------------------------->

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        submitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendPostArt();
                            }
                        });
                        break;
                    case 1:
                        submitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendPostFood();
                            }
                        });
                        break;
                    case 2:
                        submitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendPostSports();
                            }
                        });
                        break;
                    case 3:
                        submitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendPostPhotography();
                            }
                        });
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

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

            }

        });



        users.child(user_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }
    boolean mProcessLike = false;
    private void addPost()
    {
        user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference postID = users.child(user_key);
        final DatabaseReference firststep = postID.child("PostId").push();
//        postKey = getIntent().getExtras().getString("post_id");
        firststep.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (mProcessLike) {
                Log.d("Test", "Fk" + postKey);
                System.out.println(dataSnapshot.getKey());
                if (dataSnapshot.hasChild(postKey)) {
//                        delLike();
                    mProcessLike = false;
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

    }
    private void sendPostArt() {
            progress.setMessage("Posting to blog...");
            final String titleText = postTitle.getText().toString().trim();
            final String descText = postDescription.getText().toString().trim();
            final String categText = postCateg.getSelectedItem().toString().trim();

            if (!TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(descText) && imageUri != null) {
                progress.show();
                StorageReference filePath = storage.child("Blog_Images").child(imageUri.getLastPathSegment());
                filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        final DatabaseReference newPost = database.push();
                        // test categ
                        final DatabaseReference newArt = Art.push();
                        final DatabaseReference newPersonal = Posts.push();
                        // test personal Post
                        String newPersonKey = newPersonal.getKey(); // tests with newArtKey = newPost.getKey()
                        newPersonal.child("title").setValue(titleText);
                        newPersonal.child("desc").setValue(descText);
                        newPersonal.child("image").setValue(downloadUri.toString());
                        newPersonal.child("categ").setValue(categText);

//                        newPersonal.child("userKey").setValue(user_key)
//                        newPersonal
                        String newPostKey = newPost.getKey();
                        newPost.child("title").setValue(titleText);
                        newPost.child("desc").setValue(descText);
                        newPost.child("image").setValue(downloadUri.toString());
                        newPost.child("categ").setValue(categText);

                        // test categ
                        String newArtKey = newArt.getKey(); // tests with newArtKey = newPost.getKey()
                        newArt.child("title").setValue(titleText);
                        newArt.child("desc").setValue(descText);
                        newArt.child("image").setValue(downloadUri.toString());
                        newArt.child("categ").setValue(categText);

//                        final DatabaseReference new = here.child(postKey);

                        String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        newPost.child("userKey").setValue(user_key);

                        newArt.child("userKey").setValue(user_key);

                        users = FirebaseDatabase.getInstance().getReference().child("Users");
                        users.child(user_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final String u  = (String) dataSnapshot.child("name").getValue();
                                newPost.child("name").setValue(u);

                                // test categ
                                newArt.child("name").setValue(u);
                                newPersonal.child("name").setValue(u);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        progress.dismiss();
                        DatabaseReference currentUser = users.child(user_key);

                        DatabaseReference userPosts = currentUser.child("Posts");
                        final DatabaseReference temp = userPosts.child(newPostKey);

                        startActivity(new Intent(PostActivity.this, BrowserActivity.class));       //return to timeline
                    }
                });
            }
    }

    private void sendPostFood() {
        progress.setMessage("Posting to blog...");
        final String titleText = postTitle.getText().toString().trim();
        final String descText = postDescription.getText().toString().trim();
        final String categText = postCateg.getSelectedItem().toString().trim();

        if (!TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(descText) && imageUri != null) {
            progress.show();
            StorageReference filePath = storage.child("Blog_Images").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = database.push();
                    // test categ
                    final DatabaseReference newFood = Food.push();
                    final DatabaseReference newPersonal = Posts.push();
                    // test personal Post
                    String newPersonKey = newPersonal.getKey(); // tests with newArtKey = newPost.getKey()
                    newPersonal.child("title").setValue(titleText);
                    newPersonal.child("desc").setValue(descText);
                    newPersonal.child("image").setValue(downloadUri.toString());
                    newPersonal.child("categ").setValue(categText);

                    String newPostKey = newPost.getKey();
                    newPost.child("title").setValue(titleText);
                    newPost.child("desc").setValue(descText);
                    newPost.child("image").setValue(downloadUri.toString());
                    newPost.child("categ").setValue(categText);

                    // test categ
                    String newFoodKey = newFood.getKey(); // tests with newArtKey = newPost.getKey()
                    newFood.child("title").setValue(titleText);
                    newFood.child("desc").setValue(descText);
                    newFood.child("image").setValue(downloadUri.toString());
                    newFood.child("categ").setValue(categText);

//                        final DatabaseReference new = here.child(postKey);

                    String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    newPost.child("userKey").setValue(user_key);

                    newFood.child("userKey").setValue(user_key);

                    users = FirebaseDatabase.getInstance().getReference().child("Users");
                    users.child(user_key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String u  = (String) dataSnapshot.child("name").getValue();
                            newPost.child("name").setValue(u);

                            // test categ
                            newFood.child("name").setValue(u);
                            newPersonal.child("name").setValue(u);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    progress.dismiss();
                    DatabaseReference currentUser = users.child(user_key);

                    DatabaseReference userPosts = currentUser.child("Posts");
                    final DatabaseReference temp = userPosts.child(newPostKey);

                    startActivity(new Intent(PostActivity.this, BrowserActivity.class));       //return to timeline
                }
            });
        }
    }

    private void sendPostSports() {
        progress.setMessage("Posting to blog...");
        final String titleText = postTitle.getText().toString().trim();
        final String descText = postDescription.getText().toString().trim();
        final String categText = postCateg.getSelectedItem().toString().trim();

        if (!TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(descText) && imageUri != null) {
            progress.show();
            StorageReference filePath = storage.child("Blog_Images").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = database.push();
                    // test categ
                    final DatabaseReference newSports = Sports.push();

                    final DatabaseReference newPersonal = Posts.push();
                    // test personal Post
                    String newPersonKey = newPersonal.getKey(); // tests with newArtKey = newPost.getKey()
                    newPersonal.child("title").setValue(titleText);
                    newPersonal.child("desc").setValue(descText);
                    newPersonal.child("image").setValue(downloadUri.toString());
                    newPersonal.child("categ").setValue(categText);

                    String newPostKey = newPost.getKey();
                    newPost.child("title").setValue(titleText);
                    newPost.child("desc").setValue(descText);
                    newPost.child("image").setValue(downloadUri.toString());
                    newPost.child("categ").setValue(categText);

                    // test categ
                    String newSportsKey = newSports.getKey(); // tests with newArtKey = newPost.getKey()
                    newSports.child("title").setValue(titleText);
                    newSports.child("desc").setValue(descText);
                    newSports.child("image").setValue(downloadUri.toString());
                    newSports.child("categ").setValue(categText);

//                        final DatabaseReference new = here.child(postKey);

                    String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    newPost.child("userKey").setValue(user_key);

                    newSports.child("userKey").setValue(user_key);

                    users = FirebaseDatabase.getInstance().getReference().child("Users");
                    users.child(user_key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String u  = (String) dataSnapshot.child("name").getValue();
                            newPost.child("name").setValue(u);

                            // test categ
                            newSports.child("name").setValue(u);
                            newPersonal.child("name").setValue(u);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    progress.dismiss();
                    DatabaseReference currentUser = users.child(user_key);

                    DatabaseReference userPosts = currentUser.child("Posts");
                    final DatabaseReference temp = userPosts.child(newPostKey);

                    startActivity(new Intent(PostActivity.this, BrowserActivity.class));       //return to timeline
                }
            });
        }
    }

    private void sendPostPhotography() {
        progress.setMessage("Posting to blog...");
        final String titleText = postTitle.getText().toString().trim();
        final String descText = postDescription.getText().toString().trim();
        final String categText = postCateg.getSelectedItem().toString().trim();

        if (!TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(descText) && imageUri != null) {
            progress.show();
            StorageReference filePath = storage.child("Blog_Images").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = database.push();
                    // test categ
                    final DatabaseReference newPhotography = Photography.push();

                    final DatabaseReference newPersonal = Posts.push();
                    // test personal Post
                    String newPersonKey = newPersonal.getKey(); // tests with newArtKey = newPost.getKey()
                    newPersonal.child("title").setValue(titleText);
                    newPersonal.child("desc").setValue(descText);
                    newPersonal.child("image").setValue(downloadUri.toString());
                    newPersonal.child("categ").setValue(categText);

                    String newPostKey = newPost.getKey();
                    newPost.child("title").setValue(titleText);
                    newPost.child("desc").setValue(descText);
                    newPost.child("image").setValue(downloadUri.toString());
                    newPost.child("categ").setValue(categText);

                    // test categ
                    String newPhotographyKey = newPhotography.getKey(); // tests with newArtKey = newPost.getKey()
                    newPhotography.child("title").setValue(titleText);
                    newPhotography.child("desc").setValue(descText);
                    newPhotography.child("image").setValue(downloadUri.toString());
                    newPhotography.child("categ").setValue(categText);

//                        final DatabaseReference new = here.child(postKey);

                    String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    newPost.child("userKey").setValue(user_key);

                    newPhotography.child("userKey").setValue(user_key);

                    users = FirebaseDatabase.getInstance().getReference().child("Users");
                    users.child(user_key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String u  = (String) dataSnapshot.child("name").getValue();
                            newPost.child("name").setValue(u);

                            // test categ
                            newPhotography.child("name").setValue(u);
                            newPersonal.child("name").setValue(u);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    progress.dismiss();
                    DatabaseReference currentUser = users.child(user_key);

                    DatabaseReference userPosts = currentUser.child("Posts");
                    final DatabaseReference temp = userPosts.child(newPostKey);

                    startActivity(new Intent(PostActivity.this, BrowserActivity.class));       //return to timeline
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
            }
        }
    }

