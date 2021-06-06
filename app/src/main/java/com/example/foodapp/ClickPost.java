package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.microedition.khronos.egl.EGLDisplay;

public class ClickPost extends AppCompatActivity {

    private Button editButton, deleteButton;
    private TextView titleText, ingredientsText, instructionsText;

    private String accessingPostID, currentUserID, databaseUserID;

    private DatabaseReference postRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);

        titleText = findViewById(R.id.RecipeTitleData);
        ingredientsText = findViewById(R.id.txtIngredientsData);
        instructionsText = findViewById(R.id.InstructionsData);

        editButton = findViewById(R.id.btnUpdate);
        editButton.setVisibility(View.INVISIBLE);
        deleteButton = findViewById(R.id.btnDelete);
        deleteButton.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        accessingPostID = getIntent().getExtras().get("accessingPostID").toString();

        postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(accessingPostID);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCurrentPost();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateCurrentPost();
                SendUserToFoodFeed();
            }
        });

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String postTitle = snapshot.child("Title").getValue().toString();
                    String postIngredients = snapshot.child("Ingredients").getValue().toString();
                    String postInstructions = snapshot.child("Instructions").getValue().toString();

                    databaseUserID = snapshot.child("userCreatorID").getValue().toString();

                    titleText.setText(postTitle);
                    ingredientsText.setText(postIngredients);
                    instructionsText.setText(postInstructions);

                    if (currentUserID.equals(databaseUserID)){
                        editButton.setVisibility(View.VISIBLE);
                        deleteButton.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DeleteCurrentPost() {
        postRef.removeValue();
        SendUserToFoodFeed();
        Toast.makeText(this, "Post has been deleted", Toast.LENGTH_SHORT).show();

    }

    private void UpdateCurrentPost(){
        String newTitle, newIngredients, newInstructions;

        newTitle = titleText.getText().toString();
        newIngredients = ingredientsText.getText().toString();
        newInstructions = instructionsText.getText().toString();

        postRef.child("Title").setValue(newTitle);
        postRef.child("Ingredients").setValue(newIngredients);
        postRef.child("Instructions").setValue(newInstructions);

        Toast.makeText(this, "Post has been updated", Toast.LENGTH_SHORT).show();
    }
    private void SendUserToFoodFeed(){
        Intent deleteIntent = new Intent(ClickPost.this, FoodFeed.class);
        deleteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(deleteIntent);
        finish();
    }
}