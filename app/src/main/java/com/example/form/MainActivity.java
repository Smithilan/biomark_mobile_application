package com.example.form;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView createAccountButton;
    private TextView recoveryAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        recoveryAccountButton = findViewById(R.id.recoveryAccountButton);
         //Navigate to LoginActivity when Login button is clicked
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Navigate to RegistrationActivity when Create Account button is clicked
        createAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        // Navigate to UserRecoveryActivity when Recovery Account button is clicked
        recoveryAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserRecoveryActivity.class);
            startActivity(intent);
        });
    }
}
