package com.example.form;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executors;
import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private AppDatabase database;
    private BioMarkDatabase bioMarkDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Room Databases
        database = AppDatabase.getInstance(this);
        bioMarkDatabase = BioMarkDatabase.getInstance(this);

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Set up button click listener
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            } else {
                verifyUser(email, password);
            }
        });
    }

    // Method to verify user credentials and navigate accordingly
    private void verifyUser(String email, String password) {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = database.userDao().getUserByEmail(email);

            runOnUiThread(() -> {
                if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                    checkModelBuildingDataAndNavigate(email);
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void checkModelBuildingDataAndNavigate(String email) {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Check if model-building data exists for this specific user
            ModelBuildingData modelBuildingData = bioMarkDatabase.modelBuildingDao().getModelBuildingDataByEmail(email);

            Intent intent;
            if (modelBuildingData != null) {
                // Model-building data exists, navigate to ProfileActivity
                intent = new Intent(LoginActivity.this, ProfileActivity.class);
            } else {
                // No model-building data for this user, navigate to ModelBuildingActivity
                intent = new Intent(LoginActivity.this, ModelBuildingActivity.class);
            }

            // Pass the user email to the next activity
            intent.putExtra("USER_EMAIL", email);
            startActivity(intent);
            finish();
        });
    }

}
