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

public class AccountDeleteActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonUnsubscribe;
    private AppDatabase appDatabase;
    private BioMarkDatabase bioMarkDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_delete);

        // Initialize databases
        appDatabase = AppDatabase.getInstance(this);
        bioMarkDatabase = BioMarkDatabase.getInstance(this);

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonUnsubscribe = findViewById(R.id.buttonUnsubscribe);

        // Set up unsubscribe button listener
        buttonUnsubscribe.setOnClickListener(v -> verifyAndDeleteAccount());
    }

    private void verifyAndDeleteAccount() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verify credentials in a background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = appDatabase.userDao().getUserByEmail(email);
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {

                // Delete user data from AppDatabase and BioMarkDatabase
                appDatabase.userDao().delete(user);
                ModelBuildingData modelData = bioMarkDatabase.modelBuildingDao().getModelBuildingDataByEmail(email);
                if (modelData != null) {
                    bioMarkDatabase.modelBuildingDao().delete(modelData);
                }

                runOnUiThread(() -> {
                    Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                    // Navigate to login page after deletion
                    Intent intent = new Intent(AccountDeleteActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
