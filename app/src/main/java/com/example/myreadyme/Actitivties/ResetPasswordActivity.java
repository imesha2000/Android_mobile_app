package com.example.myreadyme.Actitivties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myreadyme.Database.DBHelper;
import com.example.myreadyme.R;

public class ResetPasswordActivity extends AppCompatActivity {

    Button button;
    EditText cpassword, newpassword, newpassword2;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Initialize UI elements
        newpassword = findViewById(R.id.newpassword);         // New Password EditText
        newpassword2 = findViewById(R.id.newpassword2);       // Confirm Password EditText
        db = new DBHelper(this);

        // Set click listener for save button
        button = findViewById(R.id.save1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String password = newpassword.getText().toString();
                String password2 = newpassword2.getText().toString();

                // Check if passwords match
                if (password.equals(password2)) {
                    // Get email from intent
                    String email = getIntent().getStringExtra("email");

                    // Validate password and update in the database
                    if (validatePassword(password, password2, email)) {
                        Boolean checkpassupdate = db.updatepassword(password, email);
                        if (checkpassupdate) {
                            // If password update is successful, navigate to LoginActivity
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(ResetPasswordActivity.this, "Password updated",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Failed to update password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Passwords don't match",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Password validation
    private boolean validatePassword(String newPassword, String conPassword, String email) {
        if (TextUtils.isEmpty(newPassword)) {
            newpassword.setError("Password is required!");
            newpassword.requestFocus();
            return false;
        } else {
            newpassword.setError(null);
        }

        if (newPassword.length() < 6) {
            // Check if password length is at least 6 characters
            newpassword.setError("Password must be at least 6 characters long");
            newpassword.requestFocus();
            return false;
        } else {
            newpassword.setError(null);
        }

        // Check if password contains at least one uppercase letter, one lowercase letter, and one digit
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        for (char c : newPassword.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        if (!hasUppercase || !hasLowercase || !hasDigit) {
            newpassword.setError("Password must contain at least one uppercase letter, one lowercase " +
                    "letter, and one digit");
            newpassword.requestFocus();
            return false;
        } else {
            newpassword.setError(null);
        }

        if (!newPassword.equals(conPassword)) {
            // Check if passwords match
            newpassword2.setError("Passwords do not match");
            newpassword2.requestFocus();
            return false;
        } else {
            newpassword2.setError(null);
        }

        return true;
    }
}
