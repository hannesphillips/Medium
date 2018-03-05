package com.example.calvinkwan.medium20;


import android.app.FragmentManager;
//import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class BrowserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String postKey = null;
    private TextView nameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
//        final Fragment frag = getSupportFragmentManager().findFragmentById(R.id.navigation_header_browser);
//        View myView = inflater.inflate(R.layout.nav_header_browser,container, false);
//        usersname = myView.findViewById(R.id.userName);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
        Auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null)
                {
                    Intent loginIntent = new Intent(BrowserActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
//                    nameView = (TextView) frag.getView().findViewById(R.id.email);
//                    nameView.setText(user_id);
//                    Log.d("Test",user_id);
//                    ((TextView)frag.getView().findViewById(R.id.email)).setText(firebaseAuth.getCurrentUser().getEmail());

                }else
                {
                   //getSupportFragmentManager().findFragmentById(R.id.navigation_header_browser);

//                    View view= frag.getView();
//                    if(view != null)
//                    {
//                        nameView = (TextView) view.findViewById(R.id.email);// view.findViewById(R.id.email).setText(nameView);
//                        nameView.setText("hello");

//                    nameView = (TextView) frag.getView().findViewById(R.id.email);
//                    nameView.setText(firebaseAuth.getUid());
//                    Log.d("Test",firebaseAuth.getUid());
                }
                //==TODO This doesn't work idk why whut is this shit
                /*FROM HERE
                FragmentManager frag = (FragmentManager) getFragmentManager().findFragmentById(R.id.navigation_header_browser);
                Log.d("Test", firebaseAuth.getUid());
                if(frag != null)
                {
                 frag.

//                 ((TextView)myview.findViewById(R.id.email)).setText(firebaseAuth.getUid());

                    }
                UP TO HERE */
            }
        };
        Auth.addAuthStateListener(authStateListener);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId() == R.id.action_add)
        {
            startActivity(new Intent(BrowserActivity.this, PostActivity.class));
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                postKey = extras.getString("blog_id");
                Log.d("Test","yeah" + postKey);
            }
        }

        if(item.getItemId() == R.id.action_logout)
        {
            System.out.println("esketit");
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_home) {

        fragmentManager.beginTransaction().replace(R.id.content_frame, new myPostFragment()).commit();
        }
        else if (id == R.id.nav_myPost) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new profileFragment()).commit();
        }
        else if (id == R.id.nav_bookmark) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new BookmarksFragment()).commit();
        }
        else if (id == R.id.nav_notification ){
//            fragmentManager.beginTransaction().replace(R.id.content_frame, new SecondFragment()).commit();
//        } else if (id == R.id.nav_slideshow) {

//        } else if (id == R.id.nav_manage) {

//        } else if (id == R.id.nav_share) {

//        } else if (id == R.id.nav_send) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout()
    {
        Auth.signOut();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}
