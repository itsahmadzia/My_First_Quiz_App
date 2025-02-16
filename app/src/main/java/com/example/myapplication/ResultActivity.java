package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    private TextView userNameText, scoreText;
    private Button shareButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize UI components
        userNameText = findViewById(R.id.userName);
        scoreText = findViewById(R.id.scoreText);
        shareButton = findViewById(R.id.shareButton);
        backButton = findViewById(R.id.backButton);

        // Get data from Intent
        int finalScore = getIntent().getIntExtra("finalScore", 0);
        String username = getIntent().getStringExtra("userName");

        // Set username and score
        userNameText.setText(username != null ? username : "Player");
        scoreText.setText(finalScore + "/10");

        // Share Button Click Listener
        shareButton.setOnClickListener(view -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, username + " scored " + finalScore + "/10 in the quiz app! Can you beat it?");
            startActivity(Intent.createChooser(shareIntent, "Share using"));
        });

        backButton.setOnClickListener((view )-> finish());
    }
}
