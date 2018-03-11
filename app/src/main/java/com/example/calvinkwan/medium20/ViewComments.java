package com.example.calvinkwan.medium20;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewComments extends AppCompatActivity {
    private DatabaseReference commentDatabase;
    private DatabaseReference userName;
    private String postKey;
    private FirebaseAuth Auth;
    private RecyclerView comments;
    private String name;
    private EditText enterComment;
    private Button submitComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);

        postKey = getIntent().getExtras().getString("blog_id");
        commentDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        commentDatabase = commentDatabase.child(postKey);
        commentDatabase = commentDatabase.child("Comments");
        Auth = FirebaseAuth.getInstance();

        userName = FirebaseDatabase.getInstance().getReference().child("Users");

        comments = findViewById(R.id.comments_recycler_view);
        comments.setHasFixedSize(true);
        comments.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        comments.setLayoutManager(layoutManager);

        enterComment = findViewById(R.id.enterComment);
        submitComment = findViewById(R.id.submitComment);

        enterComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // comments.setVisibility(View.INVISIBLE);
            }
        });

        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitComment();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                // comments.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerAdapter<Comment, CommentViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(

                Comment.class,
                R.layout.comment_row,
                CommentViewHolder.class,
                commentDatabase
        )
        {

            @Override
            protected void populateViewHolder(CommentViewHolder viewHolder, Comment model, int position)
            {
                viewHolder.setText(model.getText());
                viewHolder.setName(model.getName());
            }
        };

        comments.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public CommentViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        private void setText(String text) {
            TextView comment = mView.findViewById(R.id.comment);
            comment.setText(text);
        }

        private void setName(String name) {
            TextView username = mView.findViewById(R.id.username);
            username.setText(name);
        }

    }

    void submitComment() {
        String value = enterComment.getText().toString();
        if (value.matches("")) {
            Toast.makeText(ViewComments.this, "Please enter text", Toast.LENGTH_SHORT).show();
        }
        else {
            // get user key and post id
            String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DatabaseReference pushComment = commentDatabase.push();
            pushComment.child("userkey").setValue(user_key);
            pushComment.child("text").setValue(value);
            userName.child(user_key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name = (String) dataSnapshot.child("name").getValue();
                    pushComment.child("name").setValue(name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            enterComment.setText("");
        }
    }
}
