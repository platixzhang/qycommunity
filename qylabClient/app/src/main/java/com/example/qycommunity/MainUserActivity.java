package com.example.qycommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainUserActivity extends AppCompatActivity {
    private String username;
    private TextView usernameEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        username = getIntent().getStringExtra("username");
        usernameEditText = findViewById(R.id.usernameWelcome);
        usernameEditText.setText(username);
    }
}
