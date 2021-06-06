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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrentFriends extends AppCompatActivity {

    private RecyclerView FriendList;
    private DatabaseReference friendRef, userRef;
    private FirebaseAuth mAuth;
    private String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_friends);


        FriendList = findViewById(R.id.list_of_friends);

        BottomNavigationView navView = findViewById(R.id.bottomAppBarcurrentFriends);
        navView.setSelectedItemId(R.id.currentfriends);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        friendRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUserID);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        FriendList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        FriendList.setLayoutManager(linearLayoutManager);


        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                , FoodFeed.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.currentfriends:

                        return true;
                    case R.id.friendRequests:
                        startActivity(new Intent (getApplicationContext()
                                , FriendRequests.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profileFood:
                        startActivity(new Intent (getApplicationContext()
                                , Profile.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });

    }

    public void onStart(){
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<FindPeople>().setQuery(friendRef, FindPeople.class).build();

        FirebaseRecyclerAdapter<FindPeople, FriendViewHolder> adapter = new FirebaseRecyclerAdapter<FindPeople, FriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendViewHolder holder, int position, @NonNull FindPeople model) {

                String userID = getRef(position).getKey();
                userRef.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("image")){
                            String userImage = snapshot.child("image").getValue().toString();
                            String profileuserName = snapshot.child("fullName").getValue().toString();
                            String profileuserNickname = snapshot.child("Nickname").getValue().toString();


                            holder.username.setText(profileuserName);
                            holder.userNickname.setText(profileuserNickname);
                            Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(holder.profileImage);

                        }
                        else {
                            String profileuserName = snapshot.child("fullName").getValue().toString();
                            String profileuserNickname = snapshot.child("Nickname").getValue().toString();


                            holder.username.setText(profileuserName);
                            holder.userNickname.setText(profileuserNickname);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display, parent,false);
                FriendViewHolder viewHolder = new FriendViewHolder(view);
                return viewHolder;
            }
        };

        FriendList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder{
        TextView username, userNickname;
        CircleImageView profileImage;


        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.all_user_profile_name);
            userNickname = itemView.findViewById(R.id.nick_name);
            profileImage = itemView.findViewById(R.id.setProfileImage);
        }
    }
}