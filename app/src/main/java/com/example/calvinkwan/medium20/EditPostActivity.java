package com.example.calvinkwan.medium20;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Hannes Phillips on 3/5/2018.
 */

public class EditPostActivity extends AppCompatActivity {

    private EditText postTitle;
    private EditText postDescription;
    private EditText postName;
    private AdapterView.OnItemSelectedListener postCateg;

    private Button submitButton;
    private StorageReference storage;
    private DatabaseReference database;
    private DatabaseReference users;
    private DatabaseReference likes;
    private ProgressDialog progress;

    private Uri imageUri = null;
}
