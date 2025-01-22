package com.example.form;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class SuccessActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        // Retrieve the user's name from the Intent
        String firstName = getIntent().getStringExtra("FIRST_NAME");

        // Display the success message
        TextView textViewMessage = findViewById(R.id.textViewMessage);
        textViewMessage.setText("Registration successful! Welcome, " + firstName + "!");

        // Set up the Login button
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity
                Intent intent = new Intent(SuccessActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Set up the Home button
        Button buttonHome = findViewById(R.id.buttonHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MainActivity (the landing page)
                Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

