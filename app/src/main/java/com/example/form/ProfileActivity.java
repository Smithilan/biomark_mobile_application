package com.example.form;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executors;
import android.content.Intent;
import android.widget.Button;


public class ProfileActivity extends AppCompatActivity {
    private TextView textViewFullName, textViewEmail;
    private TextView textViewModelBuildingData;
    private AppDatabase appDatabase;
    private BioMarkDatabase bioMarkDatabase;
    private String email;  // Email of the currently logged-in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Retrieve email passed from the LoginActivity
        email = getIntent().getStringExtra("USER_EMAIL");

        // Initialize both databases
        appDatabase = AppDatabase.getInstance(this);
        bioMarkDatabase = BioMarkDatabase.getInstance(this);

        // Initialize UI components
        textViewFullName = findViewById(R.id.textViewFullName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewModelBuildingData = findViewById(R.id.textViewModelBuildingData);

        // Fetch User Data
        fetchUserData();

        // Fetch Model Building Data
        fetchModelBuildingData();

        // Initialize and set up Edit Profile button
        Button buttonEditProfile = findViewById(R.id.buttonEditProfile);
        buttonEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("USER_EMAIL", email);
            startActivity(intent);
        });

        // Initialize and set up Edit Profile button
        Button buttonUnSubscribe = findViewById(R.id.buttonUnSubscribe);
        buttonUnSubscribe.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AccountDeleteActivity.class);
            intent.putExtra("USER_EMAIL", email);
            startActivity(intent);
        });


    }

    private void fetchUserData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = appDatabase.userDao().getUserByEmail(email);

            runOnUiThread(() -> {
                if (user != null) {
                    textViewFullName.setText("Full Name: " + user.getFullName());
                    textViewEmail.setText("Email: " + user.getEmail());
                } else {
                    textViewFullName.setText("User not found");
                }
            });
        });
    }

    private void fetchModelBuildingData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Fetch model building data specifically for the current user's email
            ModelBuildingData modelBuildingData = bioMarkDatabase.modelBuildingDao().getModelBuildingDataByEmail(email);

            runOnUiThread(() -> {
                if (modelBuildingData != null) {
                    textViewModelBuildingData.setText("Model Building Data: \n" +
                            "DOB: " + modelBuildingData.getDob() + "\n" +
                            "Time of Birth: " + modelBuildingData.getTimeOfBirth() + "\n" +
                            "Location of Birth: " + modelBuildingData.getLocationOfBirth() + "\n" +
                            "Blood Group: " + modelBuildingData.getBloodGroup() + "\n" +
                            "Sex: " + modelBuildingData.getSex() + "\n" +
                            "Height: " + modelBuildingData.getHeight() + "\n" +
                            "Ethnicity: " + modelBuildingData.getEthnicity() + "\n" +
                            "Eye Color: " + modelBuildingData.getEyeColor());
                } else {
                    textViewModelBuildingData.setText("Model building data not provided.");
                }
            });
        });




    }
}

