package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private Button loginButton;

    private ImageButton searchButton;
    private EditText searchInput;

    private RecyclerView searchResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView navView = findViewById(R.id.bottomAppBarProfile);
        navView.setSelectedItemId(R.id.profileFood);
        loginButton = findViewById(R.id.btnLogOut2);

        searchResultList = findViewById(R.id.friend_list);
        searchResultList.setHasFixedSize(true);
        searchResultList.setLayoutManager(new LinearLayoutManager(this));

        searchButton = findViewById(R.id.searchButton);
        searchInput = findViewById(R.id.searchFriendBox);

        mFirebaseAuth = FirebaseAuth.getInstance();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchResult = searchInput.getText().toString();
                SearchFriends(searchResult);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                , FoodFeed.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profileFood:
                        return true;

                }
                return false;
            }
        });
    }

    private void SearchFriends(String searchResult) {

        FirebaseRecyclerAdapter<FindPeople, >
    }

    public static class FindPeopleViewHolder extends RecyclerView.ViewHolder{
        public FindPeopleViewHolder(@NonNull View itemView)
        {
            super(itemView);
        }
    }


    public void logout(){
        mFirebaseAuth.signOut();
        SendUserToSignin();
    }

    private void SendUserToSignin() {
        Intent intent = new Intent(Profile.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}