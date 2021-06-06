package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CreatePost extends AppCompatActivity {

    TextView title, instructions, rating, ingredients;
    Button submit;

    private String current_user_id, postTitle, postInstructions, postRating, postRandomName, dbcurrentDate, dbcurrentTime, postIngredients;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseDatabase rootNode;
    private DatabaseReference databaseUserRef, databasePostRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mFirebaseAuth = FirebaseAuth.getInstance();
        current_user_id = mFirebaseAuth.getCurrentUser().getUid();


        title = findViewById(R.id.RecipeTitleData);
        instructions = findViewById(R.id.InstructionsData);
        ingredients = findViewById(R.id.txtIngredientsData);

        submit = findViewById(R.id.btnUpdate);

        databaseUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        databasePostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavePost();
            }
        });
    }

    private void SavePost() {
        postTitle = title.getText().toString();
        postInstructions = instructions.getText().toString();
        postIngredients = ingredients.getText().toString();


        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        dbcurrentDate = currentDate.format(calDate.getTime());

        Calendar calTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        dbcurrentTime = currentTime.format(calTime.getTime());

        postRandomName = dbcurrentDate + dbcurrentTime;

     databaseUserRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
             if(snapshot.exists())
             {
                String fullName = snapshot.child("fullName").getValue().toString();


                 HashMap postMap = new HashMap();
                 postMap.put("userCreatorID", current_user_id);
                 postMap.put("Title", postTitle);
                 postMap.put("Instructions", postInstructions);
                 postMap.put("Ingredients", postIngredients);
                 postMap.put("overallRating", "0");
                 postMap.put("userName", fullName);
                 postMap.put("date", dbcurrentDate);
                 postMap.put("time", dbcurrentTime);


                 databasePostRef.child(current_user_id + postRandomName).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                     @Override
                     public void onComplete(@NonNull Task task) {
                         if(task.isSuccessful()){
                             SendUserToMainActivity();
                             Toast.makeText(CreatePost.this, "New post uploaded successfully", Toast.LENGTH_SHORT);
                         }
                         else
                             Toast.makeText(CreatePost.this, "Post failed", Toast.LENGTH_SHORT);
                     }
                 });
             }

         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });
    }

    private void SendUserToMainActivity() {
        //Getting Rid of intent flag so that when an authenticated user whos taken to the feed screen clicks back button on phone,
        // they cannot return to the setup activity as the setup activity would have been removed from the stack.
        Intent intent = new Intent(CreatePost.this, FoodFeed.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}

//        //NEED TO SORT OUT WHY ValueEventListener doesnt think class is abstract.
//        private void SubmittingDataToDatabase () {
//            UsersRef.child(current_user_id).addChildEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        if (dataSnapshot.exists()) {
//                            String foodReceipe = dataSnapshot.child("Title").getValue().toString();
//                            String instructions = dataSnapshot.child("Instructions").getValue().toString();
//                        }
//
//                    }
//                }

//               // @Override
//                public void OnCancelled(DatabaseError databaseError) {
//                }
//
//
//            });



