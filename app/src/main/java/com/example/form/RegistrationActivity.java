package com.example.form;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.mindrot.jbcrypt.BCrypt;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    private EditText editTextFullName, editTextDOB, editTextMothersMaidenName, editTextBestFriendName,
            editTextChildhoodPetName, editTextPhoneNumber, editTextEmail, editTextPassword;
    private Button buttonRegister, buttonCancel;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration); // Use the correct layout for registration

        // Initialize Room Database
        try {
            database = AppDatabase.getInstance(this);
            if (database == null) {
                throw new Exception("Database instance is null");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Database initialization failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return; // Stop if the database is not initialized
        }


        // Initialize views
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextDOB = findViewById(R.id.editTextDOB);
        editTextMothersMaidenName = findViewById(R.id.editTextMothersMaidenName);
        editTextBestFriendName = findViewById(R.id.editTextBestFriendName);
        editTextChildhoodPetName = findViewById(R.id.editTextChildhoodPetName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonCancel = findViewById(R.id.buttonCancel);

        // Check if any view is null (possible if the layout resource IDs are incorrect)
        if (editTextFullName == null || editTextDOB == null || editTextMothersMaidenName == null ||
                editTextBestFriendName == null || editTextChildhoodPetName == null || editTextPhoneNumber == null ||
                editTextEmail == null || editTextPassword == null || buttonRegister == null || buttonCancel == null) {
            Toast.makeText(this, "One or more view references are null. Check layout IDs.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable Register button initially
        buttonRegister.setEnabled(false);

        // Add TextWatchers to each EditText to monitor changes
        editTextFullName.addTextChangedListener(textWatcher);
        editTextDOB.addTextChangedListener(textWatcher);
        editTextMothersMaidenName.addTextChangedListener(textWatcher);
        editTextBestFriendName.addTextChangedListener(textWatcher);
        editTextChildhoodPetName.addTextChangedListener(textWatcher);
        editTextPhoneNumber.addTextChangedListener(textWatcher);
        editTextEmail.addTextChangedListener(textWatcher);
        editTextPassword.addTextChangedListener(textWatcher);

        buttonRegister.setOnClickListener(v -> {
            if (validateFields()) {
                buttonRegister.setEnabled(false); // Disable button during the database operation
                saveUserToDatabase(new User(
                        editTextFullName.getText().toString().trim(),
                        editTextDOB.getText().toString().trim(),
                        editTextMothersMaidenName.getText().toString().trim(),
                        editTextBestFriendName.getText().toString().trim(),
                        editTextChildhoodPetName.getText().toString().trim(),
                        editTextPhoneNumber.getText().toString().trim(),
                        editTextEmail.getText().toString().trim(),
                        editTextPassword.getText().toString().trim()
                ));
            }
        });

        // Cancel button functionality
        buttonCancel.setOnClickListener(v -> clearFields());
    }

    // Validation function for all fields
    private boolean validateFields() {
        if (TextUtils.isEmpty(editTextFullName.getText().toString().trim())) {
            editTextFullName.setError("Full name is required");
            return false;
        }
        if (TextUtils.isEmpty(editTextDOB.getText().toString().trim())) {
            editTextDOB.setError("Date of birth is required");
            return false;
        }
        if (TextUtils.isEmpty(editTextMothersMaidenName.getText().toString().trim())) {
            editTextMothersMaidenName.setError("Mother's maiden name is required");
            return false;
        }
        if (TextUtils.isEmpty(editTextBestFriendName.getText().toString().trim())) {
            editTextBestFriendName.setError("Best friend's name is required");
            return false;
        }
        if (TextUtils.isEmpty(editTextChildhoodPetName.getText().toString().trim())) {
            editTextChildhoodPetName.setError("Childhood pet's name is required");
            return false;
        }
        if (TextUtils.isEmpty(editTextPhoneNumber.getText().toString().trim()) ||
                !Pattern.matches("^\\+?[0-9]{10,13}$", editTextPhoneNumber.getText().toString().trim())) {
            editTextPhoneNumber.setError("Valid phone number is required");
            return false;
        }
        if (TextUtils.isEmpty(editTextEmail.getText().toString().trim()) ||
                !android.util.Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString().trim()).matches()) {
            editTextEmail.setError("Valid email is required");
            return false;
        }
        if (editTextPassword.getText().toString().trim().length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }

    // TextWatcher to monitor field changes
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            buttonRegister.setEnabled(validateFields());
        }
    };

    private void saveUserToDatabase(User user) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                // Check if user already exists in the database
                if (database.userDao().getUserByEmail(user.getEmail()) != null) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
                        buttonRegister.setEnabled(true);
                    });
                } else {
                    // Hash the password before storing it
                    user.setPassword(hashPassword(user.getPassword()));
                    database.userDao().insert(user);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                        clearFields();
                        Intent intent = new Intent(RegistrationActivity.this, SuccessActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Registration failed, please try again", Toast.LENGTH_SHORT).show();
                    buttonRegister.setEnabled(true);
                });
                e.printStackTrace();
            }
        });
    }

    private void clearFields() {
        editTextFullName.setText("");
        editTextDOB.setText("");
        editTextMothersMaidenName.setText("");
        editTextBestFriendName.setText("");
        editTextChildhoodPetName.setText("");
        editTextPhoneNumber.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
    }

    private String hashPassword(String password) {
        // Use a secure hashing function like BCrypt
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}