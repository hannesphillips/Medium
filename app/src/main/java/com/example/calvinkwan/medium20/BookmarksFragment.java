package com.example.calvinkwan.medium20;


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
 * A simple {@link Fragment} subclass.
 */
public class BookmarksFragment extends Fragment {
    View myView;
    private RecyclerView bookmarksView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mdatabase;
    private FirebaseAuth Auth;
    private DatabaseReference bookmarks;
    private DatabaseReference here;


    public BookmarksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_bookmarks,container, false);

        Auth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        bookmarksView = (RecyclerView) myView.findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        bookmarksView.setLayoutManager(mLayoutManager);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Blog");       //gets root URL from firebase account and gets all contents inside the blog folder in firebase

        String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference temp = FirebaseDatabase.getInstance().getReference().child("Users");
        temp = temp.child(user_key);

        // bookmarks = FirebaseDatabase.getInstance().getReference().child("Users/Bookmarks");
        bookmarks = temp.child("Bookmarks");
        // here = bookmarks.child("Bookmarks");

        // Toast.makeText(getActivity(),"hey",Toast.LENGTH_SHORT).show();

        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String count = String.valueOf(dataSnapshot.getChildrenCount());
                Toast.makeText(getActivity(),count,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String count = String.valueOf(dataSnapshot.getChildrenCount());
//                Toast.makeText(getActivity(),count,Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        // return inflater.inflate(R.layout.fragment_bookmarks, container, false);
        return myView;
    }

    public void onStart()
    {
        super.onStart();
        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                bookmarks
        )
        {

            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position)
            {
                final String post_key = getRef(position).getKey();

                // Toast.makeText(getActivity(),post_key,Toast.LENGTH_SHORT).show();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setUser(model.getUser());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // startActivity(new Intent(MainActivity.this, BlogSingle.class));
                        // Toast.makeText(getActivity(),post_key,Toast.LENGTH_SHORT).show();
                        Intent blogSingleIntent = new Intent(getActivity(), BlogSingle.class);
                        blogSingleIntent.putExtra("blog_id", post_key);
                        startActivity(blogSingleIntent);
                    }
                });
            }
        };

        bookmarksView.setAdapter(firebaseRecyclerAdapter);
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

        private void setUser(String user)
        {
            TextView postUser = mView.findViewById(R.id.postUser);
            postUser.setText(user);
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

    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((BrowserActivity) getActivity())
                .setActionBarTitle("Bookmarks");
//        ((BrowserActivity) getActivity())
//                .setActionBarTitle("Bookmarks");

    }

}
