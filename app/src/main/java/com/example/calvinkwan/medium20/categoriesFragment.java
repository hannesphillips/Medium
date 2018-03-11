package com.example.calvinkwan.medium20;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class categoriesFragment extends Fragment {
    View myView;
    private RecyclerView categoriesView;
    private RecyclerView.Adapter mAdapter;
    private DatabaseReference mdatabase;
    private DatabaseReference Art;
    private DatabaseReference Food;
    private DatabaseReference Sports;
    private DatabaseReference Photography;
    private FirebaseAuth Auth;

    public categoriesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        Auth = FirebaseAuth.getInstance();

        categoriesView = myView.findViewById(R.id.my_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        categoriesView.setLayoutManager(layoutManager);


        Art = FirebaseDatabase.getInstance().getReference().child("Art");       //gets root URL from firebase account and gets all contents inside the blog folder in firebase
        Food = FirebaseDatabase.getInstance().getReference().child("Food");
        Sports = FirebaseDatabase.getInstance().getReference().child("Sports");
        Photography = FirebaseDatabase.getInstance().getReference().child("Photography");
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Blog");


        //================================ Begin spinner ===========================================
        final Spinner mySpinner = (Spinner) myView.findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Category));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selected_val = mySpinner.getSelectedItem().toString();
//                Toast.makeText(getActivity().getApplicationContext(), selected_val,
//                        Toast.LENGTH_SHORT).show();
                switch(position) {
                    case 0:     // Art -> start art recyclerview
                        onStart1();
                        break;
                    case 1:
                        onStart2();
                        break;
                    case 2:
                        onStart3();
                        break;
                    case 3:
                        onStart4();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //=================================== End Spinner ==========================================
        return myView;
    }

    public void onStart1() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class, R.layout.blog_row, BlogViewHolder.class, Art) {
            // test if Art.child("categ") == Art
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
        categoriesView.setAdapter(firebaseRecyclerAdapter);
    }
    public void onStart2() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class, R.layout.blog_row, BlogViewHolder.class, Food) {
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
        categoriesView.setAdapter(firebaseRecyclerAdapter);

    }
    public void onStart3() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class, R.layout.blog_row, BlogViewHolder.class, Sports) {
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
        categoriesView.setAdapter(firebaseRecyclerAdapter);
    }
    public void onStart4() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class, R.layout.blog_row, BlogViewHolder.class, Photography) {
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
        categoriesView.setAdapter(firebaseRecyclerAdapter);
    }




    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setTitle(String title) {
            TextView postTitle = mView.findViewById(R.id.postTitle);
            postTitle.setText(title);
        }

        private void setUser(String user) {
            TextView postUser = mView.findViewById(R.id.postUser);
            postUser.setText(user);
        }

        private void setDesc(String desc) {
            TextView postDesc = mView.findViewById(R.id.postDesc);
            postDesc.setText(desc);
        }

        private void setImage(Context ctx, String image) {
            ImageView postImage = mView.findViewById(R.id.postImage);
            Picasso.with(ctx).load(image).into(postImage);

        }

        private void setCateg(String categ) {
            Spinner postCateg = mView.findViewById(R.id.spinner1);
            postCateg.getSelectedItem();
        }

        private void setKey(String key) {
            String postkey = key;
            Activity activity = (Activity) mView.getContext();
            Toast.makeText(activity, postkey, Toast.LENGTH_SHORT).show();
        }

    }

    public void onResume() {
        super.onResume();
        // Set title bar
        ((BrowserActivity) getActivity()).setActionBarTitle("Categories");
    }
}