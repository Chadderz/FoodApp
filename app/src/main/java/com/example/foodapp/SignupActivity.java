package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.sql.DatabaseMetaData;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    public Boolean validInput;

    public String Email, Name, Password, ReenterPass;

    private EditText  UserEmail,  UserFullName,  UserPassword,  UserReenterPassword;
    private Button EnterButton;
    private ImageButton selectImageButton;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference databaseUserRef;
    private StorageReference userProfilePicRef;

    String currentUserID;

    private static final int Photo_Picked = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();

        UserEmail = findViewById(R.id.txtEmail);
        UserFullName = findViewById(R.id.txtName);
        UserPassword = findViewById(R.id.txtPassword);
        UserReenterPassword = findViewById(R.id.txtReenterPass);
        selectImageButton = findViewById(R.id.imageButton);
        
        //userProfilePicRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        //currentUserID = mFirebaseAuth.getCurrentUser().getUid();
        //databaseUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

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

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoGalleryIntenet = new Intent();
                photoGalleryIntenet.setAction(Intent.ACTION_GET_CONTENT);
                photoGalleryIntenet.setType("image/*");
                startActivityForResult(photoGalleryIntenet, Photo_Picked);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Photo_Picked && resultCode==RESULT_OK && data!=null)
        {
            Uri ImageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1).start(this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);

            if(requestCode == RESULT_OK){
                Uri resultUri = activityResult.getUri();

                StorageReference filePath = userProfilePicRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "Profile Image Saved to storage", Toast.LENGTH_SHORT).show();

                            final String downloadImageURL = task.getResult().getStorage().toString();

                            databaseUserRef.child("Profile Image").setValue(downloadImageURL)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SignupActivity.this, "Profile Image Saved to database", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        String message = task.getException().getMessage();
                                        Toast.makeText(SignupActivity.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(SignupActivity.this, "Error occured: Image can't be cropped. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
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