package com.example.form;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executors;
import org.mindrot.jbcrypt.BCrypt;


public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextNewEmail, editTextNewPassword;
    private Button buttonSubmitEdit;
    private AppDatabase appDatabase;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        appDatabase = AppDatabase.getInstance(this);
        currentEmail = getIntent().getStringExtra("USER_EMAIL");

        editTextNewEmail = findViewById(R.id.editTextNewEmail);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        buttonSubmitEdit = findViewById(R.id.buttonSubmitEdit);

        buttonSubmitEdit.setOnClickListener(v -> updateProfile());
    }

    private void updateProfile() {
        String newEmail = editTextNewEmail.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();

        if (TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            User user = appDatabase.userDao().getUserByEmail(currentEmail);
            if (user != null) {
                user.setEmail(newEmail);
                user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));



                appDatabase.userDao().update(user);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                    // Navigate to MainActivity for re-login
                    Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
