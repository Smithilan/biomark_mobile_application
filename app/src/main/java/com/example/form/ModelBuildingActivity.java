package com.example.form;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ModelBuildingActivity extends AppCompatActivity {

    private EditText editTextDOB, editTextTimeOfBirth, editTextLocationOfBirth,
            editTextBloodGroup, editTextSex, editTextHeight, editTextEthnicity, editTextEyeColor;
    private BioMarkDatabase bioMarkDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modelbuilding);

        // Initialize EditTexts
        editTextDOB = findViewById(R.id.editTextDOB);
        editTextTimeOfBirth = findViewById(R.id.editTextTimeOfBirth);
        editTextLocationOfBirth = findViewById(R.id.editTextLocationOfBirth);
        editTextBloodGroup = findViewById(R.id.editTextBloodGroup);
        editTextSex = findViewById(R.id.editTextSex);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextEthnicity = findViewById(R.id.editTextEthnicity);
        editTextEyeColor = findViewById(R.id.editTextEyeColor);

        // Initialize Room Database
        try {
            bioMarkDatabase = BioMarkDatabase.getInstance(this);
            if (bioMarkDatabase == null) {
                throw new Exception("Database instance is null");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Database initialization failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return; // Stop if the database is not initialized
        }

        // Set up button click listener for Skip button
        findViewById(R.id.buttonSkip).setOnClickListener(view -> {
            Intent intent = new Intent(ModelBuildingActivity.this, ProfileActivity.class);
            intent.putExtra("USER_EMAIL", getIntent().getStringExtra("USER_EMAIL")); // Pass the email
            startActivity(intent);
            finish(); // Close this activity
        });

        // Set up button click listener for Submit button
        findViewById(R.id.buttonSubmit).setOnClickListener(view -> submitData());
    }

    private void submitData() {
        String email = getIntent().getStringExtra("USER_EMAIL");
        String dob = editTextDOB.getText().toString();
        String timeOfBirth = editTextTimeOfBirth.getText().toString();
        String locationOfBirth = editTextLocationOfBirth.getText().toString();
        String bloodGroup = editTextBloodGroup.getText().toString();
        String sex = editTextSex.getText().toString();
        String height = editTextHeight.getText().toString();
        String ethnicity = editTextEthnicity.getText().toString();
        String eyeColor = editTextEyeColor.getText().toString();

        if (dob.isEmpty() || timeOfBirth.isEmpty() || locationOfBirth.isEmpty() ||
                bloodGroup.isEmpty() || sex.isEmpty() || height.isEmpty() || ethnicity.isEmpty() || eyeColor.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save data with the email associated with the logged-in user
        ModelBuildingData data = new ModelBuildingData(email, dob, timeOfBirth, locationOfBirth,
                bloodGroup, sex, height, ethnicity, eyeColor);

        new Thread(() -> {
            try {
                bioMarkDatabase.modelBuildingDao().insertModelBuildingData(data);
                runOnUiThread(() -> {
                    Toast.makeText(ModelBuildingActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();

                    // Navigate to ProfileActivity
                    Intent intent = new Intent(ModelBuildingActivity.this, ProfileActivity.class);
                    intent.putExtra("USER_EMAIL", email);
                    startActivity(intent);
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(ModelBuildingActivity.this, "Error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

}
