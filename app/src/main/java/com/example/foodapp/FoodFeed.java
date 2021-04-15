package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.facebook.login.LoginManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class FoodFeed extends AppCompatActivity {
    private Button loginButton;
    private CallbackManager mCallbackManger;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_feed);

        mFirebaseAuth = FirebaseAuth.getInstance();


        loginButton = findViewById(R.id.btnLogOut);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoodFeed.this, CreatePost.class));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


    }
    public void logout(){
        mFirebaseAuth.signOut();
        SendUserToSignin();
    }
//    @Override
//    protected void onStart(){
//        super.onStart();
//
//        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
//
//        if(currentUser == null) {
//            SendUserToSignin();
//        }
//    }

    private void SendUserToSignin() {
        Intent intent = new Intent(FoodFeed.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}