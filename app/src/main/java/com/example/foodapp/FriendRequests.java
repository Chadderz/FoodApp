package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequests extends AppCompatActivity {

    private DatabaseReference FriendRequestRef, UsersRef, FriendRef;
    private FirebaseAuth mAuth;

    private RecyclerView listOfRequests;

    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        BottomNavigationView navView = findViewById(R.id.bottomAppBarcurrentFriendRequest);
        navView.setSelectedItemId(R.id.friendRequests);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        listOfRequests = findViewById(R.id.list_of_friends);
        listOfRequests.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        listOfRequests.setLayoutManager(linearLayoutManager);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        FriendRef = FirebaseDatabase.getInstance().getReference().child("Friends");





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
                        startActivity(new Intent (getApplicationContext()
                                , CurrentFriends.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.friendRequests:

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

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<FindPeople> options =
                new FirebaseRecyclerOptions.Builder<FindPeople>().setQuery(FriendRequestRef.child(currentUserID), FindPeople.class).build();

        FirebaseRecyclerAdapter<FindPeople, RequestViewHolder> adapter = new FirebaseRecyclerAdapter<FindPeople, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull FindPeople model) {

                holder.itemView.findViewById(R.id.request_accept_Button).setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.request_reject_Button).setVisibility(View.VISIBLE);

                final String list_user_id = getRef(position).getKey();

                DatabaseReference getTypeRef = getRef(position).child("request_type").getRef();

                getTypeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String type = snapshot.getValue().toString();

                            if(type.equals("received")){

                                UsersRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild("image") || (snapshot.child("image").equals(""))){
                                            final String requestUserName = snapshot.child("fullName").getValue().toString();
                                            final String requestUserNickname = snapshot.child("Nickname").getValue().toString();
                                            final String requestProfileImage = snapshot.child("image").getValue().toString();

                                            holder.userName.setText(requestUserName);
                                            holder.userNickname.setText(requestUserNickname);
                                            Picasso.get().load(requestProfileImage).placeholder(R.drawable.profile_image).into(holder.profileImage);
                                        }
                                        else{
                                            final String requestUserName = snapshot.child("fullName").getValue().toString();
                                            final String requestUserNickname = snapshot.child("Nickname").getValue().toString();

                                            holder.userName.setText(requestUserName);
                                            holder.userNickname.setText(requestUserNickname);
                                        }


                                        Button btn_accept = holder.itemView.findViewById(R.id.request_accept_Button);
                                        Button btn_reject = holder.itemView.findViewById(R.id.request_reject_Button);

                                        btn_accept.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                FriendRef.child(currentUserID).child(list_user_id).child("Friends").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            FriendRef.child(list_user_id).child(currentUserID).child("Friends").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        FriendRequestRef.child(currentUserID).child(list_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()){
                                                                                    FriendRequestRef.child(list_user_id).child(currentUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()){
                                                                                                Toast.makeText(FriendRequests.this, "Friend Saved...", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        });


                                                                    }
                                                                }
                                                            });

                                                        }
                                                    }
                                                });
                                            }
                                        });

                                        btn_reject.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                FriendRequestRef.child(currentUserID).child(list_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            FriendRequestRef.child(list_user_id).child(currentUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        Toast.makeText(FriendRequests.this, "Request Rejected...", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        });



                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display, parent, false);

                RequestViewHolder holder = new RequestViewHolder(view);
                return holder;
            }
        };

        listOfRequests.setAdapter(adapter);
        adapter.startListening();

    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder{

        TextView userName, userNickname;
        CircleImageView profileImage;
        Button acceptButton, rejectButton;


        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);


            userName = itemView.findViewById(R.id.all_user_profile_name);
            userNickname = itemView.findViewById(R.id.nick_name);

            profileImage = itemView.findViewById(R.id.setProfileImage);

            acceptButton = itemView.findViewById(R.id.request_accept_Button);
            rejectButton = itemView.findViewById(R.id.request_reject_Button);
        }
    }
}