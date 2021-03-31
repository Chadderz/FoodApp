package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    public Boolean validInput;

    private Button enter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        enter = findViewById(R.id.enter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validInput){
                    startActivity(new Intent(SignupActivity.this, CreatePost.class));
                }
                else {
                    Toast.makeText(SignupActivity.this, "Please Enter valid information!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}