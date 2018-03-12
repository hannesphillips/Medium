package com.example.calvinkwan.medium20;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.w3c.dom.Text;


/**
 * Created by Judy Lee on 2/18/2018.
 */

public class profileFragment extends Fragment {
    View myView;
    private RecyclerView profilepostView;
    private RecyclerView.Adapter mAdapter;
    private DatabaseReference mdatabase;
    private DatabaseReference musers;
    private DatabaseReference musers2;
    private FirebaseAuth Auth;
    private DatabaseReference profilePost;
    private String userKey = null;
    private boolean test = false;
    private String passedKey = null;

    private TextView postNum;
    private TextView followNum;
    private TextView followerNum;
    private TextView pageName;
    private ImageView userImg;

    private boolean mProcessFollow = false;

    private Button followbutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Auth = FirebaseAuth.getInstance();

        myView = inflater.inflate(R.layout.fragment_profile, container, false);



        profilepostView = (RecyclerView) myView.findViewById(R.id.my_post_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        profilepostView.setLayoutManager(layoutManager);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Blog");       //gets root URL from firebase account and gets all contents inside the blog folder in firebase
        musers = FirebaseDatabase.getInstance().getReference().child("Users");

        postNum =(TextView) myView.findViewById(R.id.postNumber);
        followNum =(TextView) myView.findViewById(R.id.followingNumber);
        followerNum =(TextView) myView.findViewById(R.id.followersNumber);
        pageName =(TextView) myView.findViewById(R.id.profileName);
        userImg =(ImageView) myView.findViewById(R.id.userPic);

        musers2 = FirebaseDatabase.getInstance().getReference().child("Users");
        musers2 = musers2.child(userKey);
        musers = musers.child(userKey);
        musers = musers.child("Personal Posts");
        followbutton = myView.findViewById(R.id.followbtn);

        musers2.child("Followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followerNum.setText(Long.toString(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        musers2.child("Following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followNum.setText(Long.toString(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(userKey.equals(  FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            Log.d("Name", "its is a same profile ");
            followbutton.setVisibility(myView.INVISIBLE);
        }
        musers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0)
                {
                    postNum.setText(Long.toString(dataSnapshot.getChildrenCount()));

                    musers2.child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot childSnap) {
//                            Log.d("Name",);//TODO Get the user name from the database but data isn't online
                            Log.d("Name", childSnap.toString());
                            pageName.setText(childSnap.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //this is where user image is being called
                    musers2.child("image").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot childSnap) {
//                            Log.d("Name", childSnap.getValue().toString());
//                            pageName.setText(childSnap.getValue().toString());
//                            userImg.setImageURI(downloadUri);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else
                {
                    postNum.setText(Long.toString(dataSnapshot.getChildrenCount()));

                    musers2.child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot childSnap) {
//                            Log.d("Name",);//TODO Get the user name from the database but data isn't online
                            Log.d("Name", childSnap.toString());
                            pageName.setText(childSnap.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //this is where user image is being called
                    musers2.child("image").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot childSnap) {
//                            Log.d("Name", childSnap.getValue().toString());
//                            pageName.setText(childSnap.getValue().toString());
//                            userImg.setImageURI(downloadUri);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        followbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userkey = FirebaseAuth.getInstance().getCurrentUser().getUid();

               // DatabaseReference addFollower = musers2.child(userkey);
                DatabaseReference addFollower = musers2.child("Followers");
                final DatabaseReference newFollower = addFollower.child(userkey);

                final DatabaseReference addFollowing = FirebaseDatabase.getInstance().getReference().child("Users").child(userkey);
                final DatabaseReference newFollowing = addFollowing.child("Following").child(userKey);

               // Log.d("Follower num: ","I shit u not");
                mProcessFollow = true;

                addFollower.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(mProcessFollow)
                        {
                            if(dataSnapshot.hasChild(userkey)) {
                                //Log.d("datasnapshot: ", userkey);
                                Toast.makeText(getActivity(), "Unfollowed", Toast.LENGTH_LONG).show();
                                musers2.child("Followers").child(userkey).removeValue();
                                addFollowing.child("Following").child(userKey).removeValue();
                                mProcessFollow = false;
                            }

                            else {
                                Toast.makeText(getActivity(), "Followed", Toast.LENGTH_LONG).show();
                                newFollower.child("userkey").setValue(userkey);
                                newFollowing.child("USERKEY").setValue(userKey);

                                Log.d("FOLLOW CHECK:", userkey);
                                Log.d("Follow check 2:", userKey);

                                mProcessFollow = false;

                            }
                        }
                       // followNum.setText(Long.toString(dataSnapshot.getChildrenCount()));
                       // followerNum.setText(Long.toString(dataSnapshot.getChildrenCount()));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        });



        return myView;
    }
    public void onStart()
    {
        super.onStart();
        FirebaseRecyclerAdapter<Blog, profileFragment.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, profileFragment.BlogViewHolder>(

                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                musers
        )
        {

            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setUser(model.getName());
                viewHolder.setDesc(model.getDesc());

                viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent blogSingleIntent = new Intent(getActivity(), BlogSingle.class);
                        blogSingleIntent.putExtra("blog_id", post_key);
                        blogSingleIntent.putExtra("flag", 1);
                        startActivity(blogSingleIntent);
                    }
                });
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
        ((BrowserActivity) getActivity())
                .setActionBarTitle("MyPosts");

    }

    public void setUser(String uid) {
        userKey = uid;
    }
}
