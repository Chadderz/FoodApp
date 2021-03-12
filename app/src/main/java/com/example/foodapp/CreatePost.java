package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreatePost extends AppCompatActivity {

    TextView title, instructions;
    Button refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);


        title = findViewById(R.id.txtTitle);
        instructions = findViewById(R.id.txtInstructions);
        refresh = findViewById(R.id.btnRefresh);


    }
}