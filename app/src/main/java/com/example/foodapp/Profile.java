package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private Button loginButton;

    private ImageButton searchButton;
    private EditText searchInput;

    private RecyclerView searchResultList;
    private DatabaseReference UsersRef;

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

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

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
                        startActivity(new Intent (getApplicationContext()
                                , FoodFeed.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.currentfriends:
                        startActivity(new Intent (getApplicationContext()
                                , CurrentFriends.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.friendRequests:
                        startActivity(new Intent (getApplicationContext()
                                , FriendRequests.class));
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

        Toast.makeText(this, "Searching...", Toast.LENGTH_LONG).show();

        Query searchPeopleFriends = UsersRef.orderByChild("fullName").startAt(searchResult).endAt(searchResult + "\uf8ff");

        FirebaseRecyclerOptions<FindPeople> options = new FirebaseRecyclerOptions.Builder<FindPeople>()
                .setQuery(searchPeopleFriends, FindPeople.class)
                .build();

        FirebaseRecyclerAdapter<FindPeople, FindPeopleViewHolder> adapter = new FirebaseRecyclerAdapter<FindPeople, FindPeopleViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindPeopleViewHolder holder, int position, @NonNull FindPeople model) {
                holder.userName.setText("Name: " + model.getFullName());
                holder.userNickname.setText("Nickname: " + model.getNickname());
                Picasso.get().load(model.getImage()).into(holder.profileImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String accessingUserID = getRef(position).getKey();

                        Intent profileIntent = new Intent(Profile.this, profileLayout.class);
                        profileIntent.putExtra("accessingUserID", accessingUserID);
                        startActivity(profileIntent);
                    }
                });
            }

            @NonNull
            @Override
            public FindPeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display, parent, false);
                FindPeopleViewHolder viewHolder = new FindPeopleViewHolder(view);
                return viewHolder;
            }
        };
        searchResultList.setAdapter(adapter);
        adapter.startListening();

    }

    public static class FindPeopleViewHolder extends RecyclerView.ViewHolder{

        TextView userName, userNickname;

        CircleImageView profileImage;

        public FindPeopleViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.all_user_profile_name);
            userNickname = itemView.findViewById(R.id.nick_name);
            profileImage = itemView.findViewById(R.id.setProfileImage);

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