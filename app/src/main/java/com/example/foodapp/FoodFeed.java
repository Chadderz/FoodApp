package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class FoodFeed extends AppCompatActivity {
    private Button loginButton;
    private CallbackManager mCallbackManger;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private RecyclerView listOfPosts;
    private DatabaseReference databasePostRef;
    private DatabaseReference databaseUserRef;
    private DatabaseReference ref;

    String userFullName;
    String userFullNameUsingID = "No Name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_feed);

        mFirebaseAuth = FirebaseAuth.getInstance();
        databasePostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        ref = FirebaseDatabase.getInstance().getReference();

        loginButton = findViewById(R.id.btnLogOut);
        listOfPosts = findViewById(R.id.post_list);
        listOfPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        listOfPosts.setLayoutManager(linearLayoutManager);



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

        BottomNavigationView navView = findViewById(R.id.bottomAppBar);
        navView.setSelectedItemId(R.id.home);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.profileFood:
                        startActivity(new Intent (getApplicationContext()
                                , Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.searchFood:
                        startActivity(new Intent (getApplicationContext()
                                , SearchPost.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<PostHelper> options =
                new FirebaseRecyclerOptions.Builder<PostHelper>()
                .setQuery(databasePostRef, PostHelper.class)
                .build();

        FirebaseRecyclerAdapter<PostHelper, PostViewHolder> adapter =
                new FirebaseRecyclerAdapter<PostHelper, PostViewHolder>(options)
                {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull PostHelper model) {


                holder.userID.setText(model.getUserName());
                holder.instruction.setText(model.getInstructions());
                holder.ingredients.setText(model.getIngredients());
                holder.overRating.setText("Rating: " + model.getOverallRating());
                holder.title.setText(model.getTitle());
                holder.date.setText("   " + model.getDate());
                holder.time.setText("   " + model.getTime());
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_post_layout, viewGroup, false);
                PostViewHolder viewHolder = new PostViewHolder(view);
                return viewHolder;
            }
        };
        listOfPosts.setAdapter(adapter);
        adapter.startListening();
    }




    public static class PostViewHolder extends  RecyclerView.ViewHolder{
        TextView userID, instruction, overRating, title, date, time, ingredients;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            userID = itemView.findViewById(R.id.post_profile_name);
            instruction = itemView.findViewById(R.id.post_instructions);
            ingredients = itemView.findViewById(R.id.post_ingredients);
            overRating = itemView.findViewById(R.id.post_rating);
            title = itemView.findViewById(R.id.post_title);
            date = itemView.findViewById(R.id.post_date);
            time = itemView.findViewById(R.id.post_time);
        }
    }

    public void logout(){
        mFirebaseAuth.signOut();
        SendUserToSignin();
    }

    private void SendUserToSignin() {
        Intent intent = new Intent(FoodFeed.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}