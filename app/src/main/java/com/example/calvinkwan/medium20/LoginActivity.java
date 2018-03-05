package com.example.calvinkwan.medium20;

import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "GoogleActivity";
    private EditText loginEmailField;
    private EditText loginPasswordField;
    private Button loginButton;
    private Button registration;
    private SignInButton googleBtn;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private TextView nameView;
    FirebaseAuth.AuthStateListener mAuthListener;

    //private GoogleSignInClient mGoogleSignInClient;

    private DatabaseReference database;
//    final Fragment frag = getSupportFragmentManager().findFragmentById(R.id.navigation_header_browser);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
//        setContentView(R.layout.activity_browser);

        database = FirebaseDatabase.getInstance().getReference().child("Users");

        loginEmailField = findViewById(R.id.loginEmailField);
        loginPasswordField = findViewById(R.id.loginPasswordField);
        loginButton = findViewById(R.id.loginButton);
        registration = findViewById(R.id.registrationButton);
        googleBtn = findViewById(R.id.googleBtn);

        googleBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signIn();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkLogin();
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent RegisterIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(RegisterIntent);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    startActivity(new Intent(LoginActivity.this, PostActivity.class));
                }
            }
        };

        //google signin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(LoginActivity.this, "Authentication has gone wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Intent siginIntent = new Intent(LoginActivity.this, MainActivity.class);
                            Intent siginIntent = new Intent(LoginActivity.this, BrowserActivity.class);
                            startActivity(siginIntent);
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void checkLogin()
    {
        String email = loginEmailField.getText().toString().trim();
        String password = loginPasswordField.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        checkUserExists();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Account Set Up Needed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void checkUserExists()
    {
        final String user_id = mAuth.getCurrentUser().getUid();

        database.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild(user_id))
                {
//                    frag.getView().findViewById(R.id.email).set;
//                    nameView = (TextView) frag.getView().findViewById(R.id.email);
//                    nameView.setText(user_id);
//                    Log.d("Test",user_id);
                    Intent mainIntent = new Intent(LoginActivity.this, BrowserActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account Set Up Needed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
