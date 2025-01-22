package com.example.form;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executors;

public class UserRecoveryActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextDob, editTextMothersMaidenName,
            editTextBestFriendName, editTextChildhoodPetName, editTextPhoneNumber;
    private Button buttonRecover;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recovery);

        // Initialize database
        appDatabase = AppDatabase.getInstance(this);

        // Initialize views
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextDob = findViewById(R.id.editTextDob);
        editTextMothersMaidenName = findViewById(R.id.editTextMothersMaidenName);
        editTextBestFriendName = findViewById(R.id.editTextBestFriendName);
        editTextChildhoodPetName = findViewById(R.id.editTextChildhoodPetName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonRecover = findViewById(R.id.buttonRecover);

        buttonRecover.setOnClickListener(v -> attemptRecovery());
    }

    private void attemptRecovery() {
        String fullName = editTextFullName.getText().toString().trim();
        String dob = editTextDob.getText().toString().trim();
        String mothersMaidenName = editTextMothersMaidenName.getText().toString().trim();
        String bestFriendName = editTextBestFriendName.getText().toString().trim();
        String childhoodPetName = editTextChildhoodPetName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        if (validateInput(fullName, dob)) {
            Executors.newSingleThreadExecutor().execute(() -> {
                User user = appDatabase.userDao().verifyRecoveryInfo(fullName, dob, mothersMaidenName, bestFriendName, childhoodPetName, phoneNumber);

                runOnUiThread(() -> {
                    if (user != null) {
                        // Recovery successful
                        Toast.makeText(this, "Account recovery successful. Navigating to Profile page.", Toast.LENGTH_LONG).show();

                        // Navigate to ProfileActivity
                        Intent intent = new Intent(UserRecoveryActivity.this, ProfileActivity.class);
                        intent.putExtra("USER_EMAIL", user.getEmail()); // Pass email or necessary info to ProfileActivity
                        startActivity(intent);
                        finish(); // Optional: finish this activity so the user can't go back to it
                    } else {
                        Toast.makeText(this, "Account recovery failed. Check your details.", Toast.LENGTH_LONG).show();
                    }
                });
            });
        }
    }

    private boolean validateInput(String fullName, String dob) {
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(dob)) {
            Toast.makeText(this, "Full Name and DOB are required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

