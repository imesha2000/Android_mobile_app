package com.example.myreadyme.Actitivties;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myreadyme.Database.DBHelper;
import com.example.myreadyme.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 99;
    EditText name, conpassword, regpassword, regemail;
    DBHelper db;
    Uri imagePath;
    Bitmap imageToStore;
    CircleImageView userImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI elements
        name = findViewById(R.id.name);               // EditText for name
        conpassword = findViewById(R.id.conpassword); // EditText for confirm password
        regpassword = findViewById(R.id.regpassword); // EditText for password
        regemail = findViewById(R.id.regemail);       // EditText for email
        userImage = findViewById(R.id.user);          // CircleImageView for user image

        db = new DBHelper(this); // Initialize database helper

        // Add click event to image to pick an image from local device storage
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseImage();
            }
        });

        Button button = findViewById(R.id.signup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = name.getText().toString();
                String pass = regpassword.getText().toString();
                String conpass = conpassword.getText().toString();
                String email = regemail.getText().toString();

                // Email validation
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    regemail.setError("Please enter a valid email address");
                    regemail.requestFocus();
                    return;
                }

                // Password validation
                if (pass.length() < 6) {
                    regpassword.setError("Password must be at least 6 characters long");
                    regpassword.requestFocus();
                    return;
                }

                // Password must contain at least one uppercase letter, one lowercase letter, and one digit
                boolean hasUppercase = false;
                boolean hasLowercase = false;
                boolean hasDigit = false;
                for (char c : pass.toCharArray()) {
                    if (Character.isUpperCase(c)) {
                        hasUppercase = true;
                    } else if (Character.isLowerCase(c)) {
                        hasLowercase = true;
                    } else if (Character.isDigit(c)) {
                        hasDigit = true;
                    }
                }
                if (!hasUppercase || !hasLowercase || !hasDigit) {
                    regpassword.setError("Password must contain at least one uppercase letter, one lowercase " +
                            "letter, and one digit");
                    regpassword.requestFocus();
                    return;
                }

                if (!pass.equals(conpass)) {
                    conpassword.setError("Passwords do not match");
                    conpassword.requestFocus();
                    return;
                }

                // Username validation
                if (user.length() < 6) {
                    name.setError("Username must be at least 6 characters long");
                    name.requestFocus();
                    return;
                }

                if (email.equals("") || user.equals("") || pass.equals("") || conpass.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter all the fields",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkresult = db.checkemail(email);
                    if (checkresult == false) {
                        Boolean registerresult = db.insertData(user, pass, email, imageToStore);
                        if (registerresult == true) {
                            Toast.makeText(RegisterActivity.this, "Register successfully",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "User already exists",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        TextView textView = findViewById(R.id.reg2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to choose an image from the device storage
    private void choseImage() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the result of choosing an image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imagePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                userImage.setImageBitmap(imageToStore);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
