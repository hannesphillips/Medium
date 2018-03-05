package com.example.calvinkwan.medium20;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;



/**
 * Created by Judy Lee on 2/18/2018.
 */

public class profileFragment extends Fragment {
    View myView;
    private RecyclerView profilepostView;
    private RecyclerView.Adapter mAdapter;
    // private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mdatabase;
    private DatabaseReference musers;
    private FirebaseAuth Auth;
    private DatabaseReference profilePost;
    private String postKey = null;
    private String userKey = null;
    private boolean test = false;
    public profileFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_profile, container, false);
        Auth = FirebaseAuth.getInstance();


        profilepostView = (RecyclerView) myView.findViewById(R.id.my_post_recycler);

        profilepostView.setHasFixedSize(true);
        profilepostView.setLayoutManager(new LinearLayoutManager(getActivity()));       //sets to vertical format
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        profilepostView.setLayoutManager(layoutManager);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Blog");       //gets root URL from firebase account and gets all contents inside the blog folder in firebase
        musers = FirebaseDatabase.getInstance().getReference().child("Users");
        userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        postKey =getActivity().getIntent().getExtras().getString("blog_id");

//        String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference temp = FirebaseDatabase.getInstance().getReference().child("Users");
//        temp = temp.child(user_key);
//        profilePost = temp.child("personalPost");
//        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String count = String.valueOf(dataSnapshot.getChildrenCount());
//                // Toast.makeText(getActivity(),count,Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        return myView;
    }
    public void onStart()
    {
        super.onStart();
        FirebaseRecyclerAdapter<Blog, profileFragment.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, profileFragment.BlogViewHolder>(

                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mdatabase
        )
        {

            @Override
            protected void populateViewHolder(final profileFragment.BlogViewHolder viewHolder, Blog model, int position) {
                final String post_key = getRef(position).getKey();
                test = (viewHolder.sameUserKey(post_key));
                Log.d("Test" , "Fkkk it worked" + test);
                if (test)
                {

                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());  //passing image as string link
                    // viewHolder.setUser(model.getUser());
                    viewHolder.setUser(model.getName());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // startActivity(new Intent(MainActivity.this, BlogSingle.class));
                            // Toast.makeText(MainActivity.this, post_key, Toast.LENGTH_LONG).show();
                            Intent blogSingleIntent = new Intent(getActivity(), BlogSingle.class);
                            blogSingleIntent.putExtra("blog_id", post_key);

                            startActivity(blogSingleIntent);
                        }
                    });
                    test = false;
              }


//                bookmark.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(getActivity(), "Bookmarked", Toast.LENGTH_LONG).show();
//                    }
//                });
            }
        };

        profilepostView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }
        private void setTitle(String title)
        {
            TextView postTitle = mView.findViewById(R.id.postTitle);
            postTitle.setText(title);
        }
        private boolean sameUserKey(String key)
        {
            boolean value = false;
            String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if(user_key == key)
            {
                value = true;
                Log.d("Test", "Real shit " + key + " and other one" + user_key);
            }
            return value;
        }
        private void setDesc(String desc)
        {
            TextView postDesc = mView.findViewById(R.id.postDesc);
            postDesc.setText(desc);
        }

        private void setImage(Context ctx, String image)
        {
            ImageView postImage = mView.findViewById(R.id.postImage);
            Picasso.with(ctx).load(image).into(postImage);

        }

        private void setUser(String user)
        {
            TextView postUser = mView.findViewById(R.id.postUser);
            postUser.setText(user);
        }

    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((BrowserActivity) getActivity())
                .setActionBarTitle("MyPosts");
//        ((BrowserActivity) getActivity())
//                .setActionBarTitle("Bookmarks");

    }

//}
    private void addtopersonal()
    {
        String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference addBookmark = musers.child(user_key);
        DatabaseReference here = addBookmark.child("Bookmarks");

        // if postkey in bookmarks, unbookmark

        // final DatabaseReference newBookmark = here.push();
        final DatabaseReference newBookmark = here.child(postKey);
    }
}
