package com.example.calvinkwan.medium20;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessageActivity extends AppCompatActivity {

   private FirebaseAuth AuthUI;
   private static int SIGN_IN_REQUEST_CODE = 1;
   private FirebaseListAdapter<publicMessageChat> adapter;
   RelativeLayout activity_main;
   FloatingActionButton fab;

   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
       MenuInflater findMenuItems = getMenuInflater();
       findMenuItems.inflate(R.menu.messagemenu, menu);
       return super.onCreateOptionsMenu(menu);
   }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionGoHome:
                Intent aboutIntent = new Intent(MessageActivity.this, BrowserActivity.class);
                startActivity(aboutIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data)
   {
       super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == SIGN_IN_REQUEST_CODE)
       {
           if(resultCode == RESULT_OK)
           {
               Snackbar.make(activity_main, "Successfully signed in, Welcome!!", Snackbar.LENGTH_SHORT).show();
               //displayChatMessage();
           }
           else
           {
               Snackbar.make(activity_main, "Could not sign you in", Snackbar.LENGTH_SHORT).show();
           }
       }
   }

   @Override
    protected void onCreate(Bundle savedInstanceState)
   {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_message);
        final String tempUserID =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("Message", "This shoukd be user key " + tempUserID);
       activity_main = findViewById(R.id.messageActivity);
       fab = findViewById(R.id.fab);
       fab.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {
                EditText input = findViewById(R.id.input);
                if(input.length() > 0) {
                    FirebaseDatabase.getInstance().getReference().push().setValue(new publicMessageChat(tempUserID, input.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                    input.setText("");
                }
                else
                {
                    Toast.makeText(MessageActivity.this, "Please enter in a message", Toast.LENGTH_LONG).show();
                }
           }
       });
       //check if not signed in
       if(FirebaseAuth.getInstance().getCurrentUser() == null)
       {
           Toast.makeText(this, "Sign in is required", Toast.LENGTH_LONG).show();
       }
       else
       {
            //Snackbar.make(activity_main, "Welcome "+ FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();

       }

       //load content
       displayChatMessage();
   }

   private void displayChatMessage()
   {
        final ListView listOfMessages = findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<publicMessageChat>(this, publicMessageChat.class, R.layout.list_of_items, FirebaseDatabase.getInstance().getReference())
        {
            @Override
            protected void populateView(View v, publicMessageChat model, int position)
            {
                TextView messageText, messageUser, messageTime;
                messageText = v.findViewById(R.id.message_text);
                messageUser = v.findViewById(R.id.message_user);
                messageTime = v.findViewById(R.id.message_time);
                final String messageID = getRef(position).getKey();
                //condition outputs - if user has child name userKey, say yes
                DatabaseReference temp = getRef(position);
                temp = temp.child("left");
                final String tempID  = temp.getKey();
//                if()
//                if(model.getMessageId()!=null) {
                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageUser());
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
//                }
//                else {
                    Log.d("Message"," Yes " + temp + " this is user " + model.getMessageId());
//
//                }

//                if( FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messageID))
//                {
//                    Log.d("Message"," Yes right side");
//                }


            }
        };
//        final ListView lv  (ListView) findView
        listOfMessages.setAdapter(adapter);

//        listOfMessages.
   }
}

