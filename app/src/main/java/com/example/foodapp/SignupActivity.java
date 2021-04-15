package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.DatabaseMetaData;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    public Boolean validInput;

    public String Email, Name, Password, ReenterPass;

    private EditText  UserEmail,  UserFullName,  UserPassword,  UserReenterPassword;
    private Button EnterButton;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference databaseUserRef;

    String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();

        UserEmail = findViewById(R.id.txtEmail);
        UserFullName = findViewById(R.id.txtName);
        UserPassword = findViewById(R.id.txtPassword);
        UserReenterPassword = findViewById(R.id.txtReenterPass);

        EnterButton = findViewById(R.id.btnEnter);

        EnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAuthentication();

               // SendUserToFeed();

//                if (validInput){
//                    startActivity(new Intent(SignupActivity.this, CreatePost.class));
//                }
//                else {
//                    Toast.makeText(SignupActivity.this, "Please Enter valid information!", Toast.LENGTH_SHORT).show();
//                }
            }
        });

    }

    private void CreateAuthentication() {


        Email = UserEmail.getText().toString();
        Name = UserFullName.getText().toString();
        Password = UserPassword.getText().toString();
        ReenterPass = UserReenterPassword.getText().toString();


        //Code to Validate Fields
        if (TextUtils.isEmpty(Email)){
            Toast.makeText(this, "Please type an Email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Name)){
            Toast.makeText(this, "Please type your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Password)){
            Toast.makeText(this, "Please type a Password...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(ReenterPass)){
            Toast.makeText(this, "Please reenter typed password...", Toast.LENGTH_SHORT).show();
        }
        else if (!Password.equals(ReenterPass)){
            Toast.makeText(this, "Please make sure that the passwords you typed match...", Toast.LENGTH_SHORT).show();
        }
        else{
            mFirebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful()){
                        Toast.makeText(SignupActivity.this, "You have successfully been authenticated!", Toast.LENGTH_SHORT).show();
                        CreateAccount();
                    }
                    else {
                        String taskMessage = task.getException().getMessage();
                        Toast.makeText(SignupActivity.this, "Error: " + taskMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void CreateAccount(){
        currentUserID = mFirebaseAuth.getCurrentUser().getUid();
        databaseUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        HashMap userMap = new HashMap();
        userMap.put("Username", Name);
        userMap.put("Password", Password);
        databaseUserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    SendUserToFeed();
                    Toast.makeText(SignupActivity.this, "You have successfully been created an Account!", Toast.LENGTH_SHORT).show();
                }
                else{
                    String taskMessage = task.getException().getMessage();
                    Toast.makeText(SignupActivity.this, "Error: " + taskMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SendUserToFeed(){
        //Getting Rid of intent flag so that when an authenticated user whos taken to the feed screen clicks back button on phone,
        // they cannot return to the setup activity as the setup activity would have been removed from the stack.
        Intent intent = new Intent(SignupActivity.this, FoodFeed.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    //Checks if User is already logged in before signup process, if so then it directs them to feed page.
    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

        if(currentUser != null) {
            SendUserToFeed();
        }
    }


}