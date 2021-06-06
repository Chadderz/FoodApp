package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class PostComments extends AppCompatActivity {


    private RecyclerView listofComments;
    private EditText CommentInputText;
    private ImageButton PostCommentButton;

    DatabaseReference UserRef, PostRef;
    FirebaseAuth mFirebaseAuth;

    String postRandomName, dbcurrentTime, dbcurrentDate;

    private String Post_Key, current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);

        Post_Key = getIntent().getExtras().get("accessingPostID").toString();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(Post_Key).child("comments");

        mFirebaseAuth = FirebaseAuth.getInstance();
        current_user_id = mFirebaseAuth.getCurrentUser().getUid();

        listofComments = findViewById(R.id.list_of_comments);
        CommentInputText = findViewById(R.id.comment_input);
        PostCommentButton = findViewById(R.id.post_comment_btn);

        listofComments.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        listofComments.setLayoutManager(linearLayoutManager);


        PostCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String userName = snapshot.child("fullName").getValue().toString();

                            ValidateComment(userName);

                            CommentInputText.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        });
        
    }

    private void ValidateComment(String userName) {
        String commentText = CommentInputText.getText().toString();

        if(TextUtils.isEmpty(commentText)){
            Toast.makeText(this, "Please write text to comment", Toast.LENGTH_LONG).show();
        }
        else{
            Calendar calDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            dbcurrentDate = currentDate.format(calDate.getTime());

            Calendar calTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            dbcurrentTime = currentTime.format(calTime.getTime());



            postRandomName = dbcurrentDate + dbcurrentTime;

            HashMap commentsMap = new HashMap();
            commentsMap.put("userPosterID", current_user_id);
            commentsMap.put("commentText", commentText);
            commentsMap.put("date", dbcurrentDate);
            commentsMap.put("time", dbcurrentTime);
            commentsMap.put("userName", userName);
            PostRef.child(current_user_id + postRandomName).updateChildren(commentsMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(PostComments.this, "You have commented successfully", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(PostComments.this, "Error occured try again", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Comments> options = new FirebaseRecyclerOptions.Builder<Comments>()
                .setQuery(PostRef, Comments.class)
                .build();

        FirebaseRecyclerAdapter<Comments, FindCommentViewHolder> adapter = new FirebaseRecyclerAdapter<Comments, FindCommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindCommentViewHolder holder, int position, @NonNull Comments model) {
                holder.commentUsername.setText(model.getUserName());
                holder.commentText.setText(model.getCommentText());
                holder.commentTime.setText(model.getTime());
                holder.commentDate.setText(model.getDate());
            }

            @NonNull
            @Override
            public FindCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_comments_layout, parent, false);
                FindCommentViewHolder viewHolder = new FindCommentViewHolder(view);
                return viewHolder;

            }
        };

        listofComments.setAdapter(adapter);
        adapter.startListening();
    }

    public static class FindCommentViewHolder extends RecyclerView.ViewHolder{
        TextView commentText, commentTime, commentDate, commentUsername;


        public FindCommentViewHolder(@NonNull View itemView) {
            super(itemView);

            commentUsername = itemView.findViewById(R.id.comment_username);
            commentText = itemView.findViewById(R.id.comment_text);
            commentTime = itemView.findViewById(R.id.comment_time);
            commentDate = itemView.findViewById(R.id.comment_date);

        }
    }
}