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
    public profileFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_profile, container, false);
        Auth = FirebaseAuth.getInstance();
        profilepostView = (RecyclerView) myView.findViewById(R.id.my_post_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        profilepostView.setLayoutManager(layoutManager);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Blog");       //gets root URL from firebase account and gets all contents inside the blog folder in firebase
        musers = FirebaseDatabase.getInstance().getReference().child("Users");
//        postKey =getActivity().getIntent().getExtras().getString("blog_id");

        String user_key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference temp = FirebaseDatabase.getInstance().getReference().child("Users");
        temp = temp.child(user_key);
        profilePost = temp.child("personalPost");
        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String count = String.valueOf(dataSnapshot.getChildrenCount());
                // Toast.makeText(getActivity(),count,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return myView;
    }
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
