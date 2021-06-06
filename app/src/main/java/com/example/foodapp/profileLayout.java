package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;



public class profileLayout extends AppCompatActivity {

    private String receiverUserID, senderUserID, currentState;

    private CircleImageView userProfileImage;
    private FirebaseAuth mAuth;

    private TextView userProfileName, userNickname;
    private Button SendFriendRequest, RejectFriendReqest;

    private DatabaseReference userRef, friendRequestRef, FriendRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);
        mAuth = FirebaseAuth.getInstance();
        receiverUserID = getIntent().getExtras().get("accessingUserID").toString();



        userProfileImage = findViewById(R.id.visitProfileImage);
        userProfileName = findViewById(R.id.visit_user_name);
        userNickname = findViewById(R.id.visit_user_nickname);
        SendFriendRequest = findViewById(R.id.send_friend_request);
        RejectFriendReqest = findViewById(R.id.reject_friend_request);
        currentState = "new";

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        friendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
        FriendRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        senderUserID = mAuth.getCurrentUser().getUid();

        RetrieveUserInfo();
    }

    private void RetrieveUserInfo() {
        userRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && (snapshot.hasChild("image"))){
                    String userImage = snapshot.child("image").getValue().toString();
                    String userName = snapshot.child("fullName").getValue().toString();
                    String Nickname = snapshot.child("Nickname").getValue().toString();

                    Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(userProfileImage);
                    userProfileName.setText(userName);
                    userNickname.setText(Nickname);



                    ManageFriendRequests();


                }
                else{
                    String userName = snapshot.child("fullName").getValue().toString();
                    String Nickname = snapshot.child("Nickname").getValue().toString();

                    userProfileName.setText(userName);
                    userNickname.setText(Nickname);

                    ManageFriendRequests();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ManageFriendRequests() {


        friendRequestRef.child(senderUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(receiverUserID)){
                    String request_type = snapshot.child(receiverUserID).child("request_type").getValue().toString();

                    if (request_type.equals("sent")){
                        currentState = "request_sent";
                        SendFriendRequest.setText("Cancel Friend Request");
                    }
                    else if (request_type.equals("received")){
                        currentState = "request_received";
                        SendFriendRequest.setText("Accept Friend Request");

                        RejectFriendReqest.setVisibility(View.VISIBLE);
                        RejectFriendReqest.setEnabled(true);
                        
                        RejectFriendReqest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CancelFriendRequest();
                            }
                        });
                    }
                }
                else{
                    FriendRef.child(senderUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(receiverUserID)){
                                currentState = "friends";
                                SendFriendRequest.setText("Remove this Friend");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(!senderUserID.equals(receiverUserID)){

            SendFriendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendFriendRequest.setEnabled(false);

                    if (currentState.equals("new")){

                        FriendRequest();

                    }
                    if(currentState.equals("request_sent")){
                        CancelFriendRequest();
                    }
                    if(currentState.equals("request_received")){
                        AcceptFriendRequest();
                    }
                    if(currentState.equals("friends")){
                        RemoveSpecificFriend();
                    }

                }
            });
        }
        else{
            SendFriendRequest.setVisibility(View.INVISIBLE);
        }
    }

    private void RemoveSpecificFriend() {
        FriendRef.child(senderUserID).child(receiverUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FriendRef.child(receiverUserID).child(senderUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                SendFriendRequest.setEnabled(true);
                                currentState="new";
                                SendFriendRequest.setText("Send Friend Request");

                                RejectFriendReqest.setVisibility(View.INVISIBLE);
                                RejectFriendReqest.setEnabled(false);
                            }
                        }
                    });

                }

            }
        });
    }

    //If request is accepted, the nesting is removing the friend request from the friend request node of the database
    private void AcceptFriendRequest() {
            FriendRef.child(senderUserID).child(receiverUserID).child("Friends").setValue("Saved Friend").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        FriendRef.child(receiverUserID).child(senderUserID).child("Friends").setValue("Saved Friend").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    friendRequestRef.child(senderUserID).child(receiverUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                              if (task.isSuccessful()){
                                                  friendRequestRef.child(receiverUserID).child(senderUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                      @Override
                                                      public void onComplete(@NonNull Task<Void> task) {
                                                            SendFriendRequest.setEnabled(true);
                                                            currentState = "friends";
                                                            SendFriendRequest.setText("Remove this Friend");

                                                            RejectFriendReqest.setVisibility(View.INVISIBLE);
                                                            RejectFriendReqest.setEnabled(false);

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

    private void CancelFriendRequest() {
        friendRequestRef.child(senderUserID).child(receiverUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    friendRequestRef.child(receiverUserID).child(senderUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                SendFriendRequest.setEnabled(true);
                                currentState="new";
                                SendFriendRequest.setText("Send Friend Request");

                                RejectFriendReqest.setVisibility(View.INVISIBLE);
                                RejectFriendReqest.setEnabled(false);
                            }
                        }
                    });

                }

            }
        });
    }

    private void FriendRequest() {

        friendRequestRef.child(senderUserID).child(receiverUserID).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    friendRequestRef.child(receiverUserID).child(senderUserID).child("request_type")
                            .setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                SendFriendRequest.setEnabled(true);
                                currentState = "request_sent";
                                SendFriendRequest.setText("Cancel Friend Request");
                            }
                        }
                    });
                }
            }
        });

    }
}