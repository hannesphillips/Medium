package com.example.calvinkwan.medium20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommentActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button submit;
    private EditText commentBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentBox = findViewById(R.id.comment);
        submit = findViewById(R.id.submitComment);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = commentBox.getText().toString();
                if (value.matches("")) {
                    Toast.makeText(CommentActivity.this, "Please Enter Text", Toast.LENGTH_LONG).show();
                }
                else {
                    // Toast.makeText(CommentActivity.this, value, Toast.LENGTH_LONG).show();
                    String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String postKey = postKey = getIntent().getExtras().getString("blog_id");
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
                    mDatabase = mDatabase.child(postKey);
                    mDatabase = mDatabase.child("Comments");
                    DatabaseReference pushComment = mDatabase.push();
                    pushComment.child(user_key).setValue(value);
                    finish();
                }
            }
        });
    }
}
