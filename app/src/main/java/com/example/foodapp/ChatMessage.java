package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessage extends AppCompatActivity {

    private String messageReceiverID, messageReceiverName, messageReceiverImage, messageReceiverNickname, messageSenderID;

    private TextView ProfileName, Nickname;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private ImageButton SendMessageButton;
    private EditText MessageInputText;

    CircleImageView ProfileImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);

        mAuth = FirebaseAuth.getInstance();
        messageSenderID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();


        messageReceiverID = getIntent().getExtras().get("visit_userID").toString();
        messageReceiverName = getIntent().getExtras().get("visit_userName").toString();
        messageReceiverImage = getIntent().getExtras().get("visit_image").toString();
        messageReceiverNickname = getIntent().getExtras().get("visit_userNickname").toString();


        ProfileName = findViewById(R.id.custom_profile_name);
        Nickname = findViewById(R.id.custom_Nickname);
        ProfileImage = findViewById(R.id.custom_profile_image);

        SendMessageButton = findViewById(R.id.post_message_btn);
        MessageInputText = findViewById(R.id.message_friend_input);

        ProfileName.setText(messageReceiverName);
        Nickname.setText(messageReceiverNickname);
        Picasso.get().load(messageReceiverImage).placeholder(R.drawable.profile_image).into(ProfileImage);




        Toast.makeText(ChatMessage.this, messageReceiverID, Toast.LENGTH_SHORT).show();
        Toast.makeText(ChatMessage.this, messageReceiverName, Toast.LENGTH_SHORT).show();


        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });
    }


    //Message Structure. Message SenderID -> Message ReceiverID -> UniqueMessageID -> (the message itself)
    private void SendMessage() {
        String messageText = MessageInputText.getText().toString();

        if(TextUtils.isEmpty(messageText)){
            Toast.makeText(ChatMessage.this, "Please write a message..", Toast.LENGTH_SHORT).show();
        }
        else{
            String messageSenderRef = "messages/" + messageSenderID + "/" + messageReceiverID;
            String messageReceiverRef = "messages/" + messageReceiverID + "/" + messageSenderID;

            DatabaseReference userMessageKeyRef = RootRef.child("messages").child(messageSenderID).child(messageReceiverID).push();

            String messagePushID = userMessageKeyRef.getKey();

            Map messageTextBody = new HashMap<>();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", messageSenderID);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put(messageReceiverRef + "/" + messagePushID, messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ChatMessage.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ChatMessage.this, "Error: Message unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                    MessageInputText.setText("");
                }
            });


        }
    }
}