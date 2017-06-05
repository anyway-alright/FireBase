package com.example.firebasetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    Button button;
    EditText editText;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String userName;
    ListView messageList;
    List<FriendlyMessage> messagesArray;

    public static final int RC_SIGN_IN=1;


    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseListAdapter<FriendlyMessage> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName="anonymous";

         database=FirebaseDatabase.getInstance();

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=database.getReference().child("child");
        messageList= (ListView) findViewById(R.id.message_list);

        messagesArray=new LinkedList<>();

        final ArrayAdapter<FriendlyMessage> adapter=new ArrayAdapter<FriendlyMessage>(this,android.R.layout.two_line_list_item,messagesArray){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if(convertView==null)
                {
                   convertView= getLayoutInflater().inflate(android.R.layout.two_line_list_item,parent,false);
                }

                FriendlyMessage chat=messagesArray.get(position);

                ((TextView)convertView.findViewById(android.R.id.text1)).setText(chat.getName());
                ((TextView)convertView.findViewById(android.R.id.text2)).setText(chat.getText());
                return convertView;

            }
        };
        messageList.setAdapter(adapter);


        button=(Button)findViewById(R.id.msg_button);
        editText= (EditText) findViewById(R.id.edit_msg);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendlyMessage friendlyMessage = new FriendlyMessage(editText.getText().toString(),userName);
                databaseReference.push().setValue(friendlyMessage);
                editText.setText("");
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FriendlyMessage friendlyMessage=dataSnapshot.getValue(FriendlyMessage.class);
                adapter.add(friendlyMessage);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null)
                {

                }
                else {
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                            .setProviders(AuthUI.EMAIL_PROVIDER,AuthUI.FACEBOOK_PROVIDER).build(),RC_SIGN_IN);
                }
            }
        });

      /*  authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    
                    onSignedInInitialize(user.getDisplayName());
                    Toast.makeText(MainActivity.this,"Singed in",Toast.LENGTH_LONG).show();
                }
                else {
                    onSingedOutCleanUp();
                    startActivityForResult(AuthUI.getInstance().
                            createSignInIntentBuilder().setIsSmartLockEnabled(false).
                            setProviders(AuthUI.EMAIL_PROVIDER,
                            AuthUI.GOOGLE_PROVIDER
                    ).build(),RC_SIGN_IN);
                }

            }
        };

    }

    private void onSingedOutCleanUp() {

        userName="anonymous";
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {

    }

    private void onSignedInInitialize(String displayName) {

        userName=displayName;

        attachDatabaseReadListener();

    }

    private void attachDatabaseReadListener() {
        adapter=new FirebaseListAdapter<FriendlyMessage>
                (this,FriendlyMessage.class,android.R.layout.two_line_list_item,databaseReference) {
            @Override
            protected void populateView(View v, FriendlyMessage model, int position) {
                ((TextView)v.findViewById(android.R.id.text1)).setText(model.getName());
                ((TextView)v.findViewById(android.R.id.text2)).setText(model.getText());

            }
        };
        messageList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
       menuInflater.inflate(R.menu.menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.sing_out:
                AuthUI.getInstance().signOut(this);
                return true;
                default:
                    return super.onOptionsItemSelected(item);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN)
            if(resultCode==RESULT_OK)
            {
                Toast.makeText(this,"Singned in",Toast.LENGTH_LONG).show();

            }
        else if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(this,"Singed in Cancelled",Toast.LENGTH_LONG).show();
                finish();
            }*/



    }
}
