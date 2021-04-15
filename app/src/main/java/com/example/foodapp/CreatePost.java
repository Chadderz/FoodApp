package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreatePost extends AppCompatActivity {

    TextView title, instructions, rating;
    Button submit;

    private String current_user_id;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseDatabase rootNode;
    private DatabaseReference UsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mFirebaseAuth = FirebaseAuth.getInstance();
        current_user_id = mFirebaseAuth.getCurrentUser().getUid();


        title = findViewById(R.id.txtRecipeTitle);
        instructions = findViewById(R.id.txtInstructions);
        rating = findViewById(R.id.txtOverallRating);
        submit = findViewById(R.id.btnSubmit);

//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rootNode = FirebaseDatabase.getInstance();
//                UsersRef = rootNode.getReference("Recipe");
//
//                String receipeTitle = title.getEditableText().toString();
//                String receipeOverallRating = rating.getEditableText().toString();
//                String receipeInstructions = instructions.getEditableText().toString();

        //    PostHelper postHelper = new PostHelper(receipeTitle, receipeOverallRating, receipeInstructions, "0");

//                UsersRef.child("1").setValue(postHelper);
//
//                Toast.makeText(CreatePost.this, "Posting Recipe", Toast.LENGTH_SHORT).show();
//
//                Intent intent2 = new Intent(CreatePost.this, FoodFeed.class);
//                startActivity(intent2);


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



