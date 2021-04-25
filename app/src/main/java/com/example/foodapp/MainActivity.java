package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private CallbackManager mCallbackManger;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private TextView textViewUser;
    private ImageView mLogo;
    private LoginButton loginButton;
 //   private AccessTokenTracker accessTokenTracker;
   // private static final String TAG = "FacebookAuthentication";
    public Boolean validCredentials;

    private DatabaseReference databaseUserRef;

    Button signup, signin;

    private TextView UserEmail, UserPassword;

    public String Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseUserRef = FirebaseDatabase.getInstance().getReference().child("Users");


        UserEmail = findViewById(R.id.txtUserEmail);
        UserPassword = findViewById(R.id.txtPassword);

        signup = findViewById(R.id.btnSignup);
        signin = findViewById(R.id.btnSignin);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //This comentted out code causes a crash, find out what it is 
               // CheckUserExists();
                SignIn();
            }
        });

        signup.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            SendUserToSignUp();
        }
        });
    };

    private void CheckUserExists() {
        final String currentUserID = mFirebaseAuth.getCurrentUser().getUid();

        databaseUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Checks if record is not in database
                if (!snapshot.hasChild(currentUserID)){
                    SendUserToSignUp();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SendUserToSignUp() {
        Intent setupIntent = new Intent(MainActivity.this, SignupActivity.class);
        setupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setupIntent);
        finish();
    }

    void SignIn(){
        Email = UserEmail.getText().toString();
        Password = UserPassword.getText().toString();

        if(!Email.isEmpty() && !Password.isEmpty()){
            mFirebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "You have successfully been authenticated!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent (MainActivity.this, FoodFeed.class));
                        finish();

                    }
                    else {
                        String taskMessage = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Error: " + taskMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
    //Checks if user is already logged in and if so will take them to the feed page.
//    @Override
//    protected void onStart(){
//        super.onStart();
//
//        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
//
//        if(currentUser != null) {
//            SendUserToFeed();
//        }
//    }

    private void SendUserToFeed(){
        //Getting Rid of intent flag so that when an authenticated user whos taken to the feed screen clicks back button on phone,
        // they cannot return to the setup activity as the setup activity would have been removed from the stack.
        Intent feedIntent = new Intent(MainActivity.this, FoodFeed.class);
        feedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(feedIntent);
        finish();
    }


//onCreate method code /////////////////////////////////////////////////////
    //TESTING STUFF
//        Intent intent = new Intent(MainActivity.this, CreatePost.class);
//        startActivity(intent);


//        mFirebaseAuth = FirebaseAuth.getInstance();
//        FacebookSdk.getApplicationContext();
//
//        textViewUser = findViewById(R.id.text_user);
//        mLogo = findViewById(R.id.imgUser);
//        loginButton = findViewById(R.id.login_button);
//        loginButton.setReadPermissions("email", "public_profile");
//        mCallbackManger = CallbackManager.Factory.create();
//        loginButton.registerCallback(mCallbackManger, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG, "onSuccess" + loginResult);
//                handleFacebookToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d(TAG, "onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d(TAG, "onSuccess" + error);
//            }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    updateUI(user);
//                }
//                else{
//                    updateUI(null);
//                }
//
//            }
//        };
//
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//                if (currentAccessToken == null)
//                {
//                    mFirebaseAuth.signOut();
//                }
//            }
//        };
//    }
//
//    private void handleFacebookToken(AccessToken token){
//        Log.d(TAG, "handleFacebookToken" + token);
//
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//
//                    Log.d(TAG, "sign in with credential: successful");
//                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
//                    updateUI(user);
//                    Intent intent = new Intent(MainActivity.this, FoodFeed.class);
//                    startActivity(intent);
//                }else {
//                    Log.d(TAG, "sign in with credential: failure", task.getException());
//                    Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
//                    updateUI(null);
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        mCallbackManger.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    private void updateUI(FirebaseUser user) {
//        if(user != null){
//            textViewUser.setText(user.getDisplayName());
//
//            if(user.getPhotoUrl() != null){
//                String photoUrl = user.getPhotoUrl().toString();
//                photoUrl = photoUrl + "?type=large";
//                Picasso.get().load(photoUrl).into(mLogo);
//            }
//        }
//        else {
//            textViewUser.setText("No Name");
//            mLogo.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mFirebaseAuth.addAuthStateListener(authStateListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        if(authStateListener != null){
//            mFirebaseAuth.removeAuthStateListener(authStateListener);
//        }
//    }
}