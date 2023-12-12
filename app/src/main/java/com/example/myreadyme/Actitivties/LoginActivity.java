package com.example.myreadyme.Actitivties;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myreadyme.Database.DBHelper;
import com.example.myreadyme.R;
import com.example.myreadyme.TodoActitivites.PersonalActivity;
import com.example.myreadyme.TodoActitivites.StudyActivity;

public class LoginActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    EditText email, password;
    SharedPreferences sharedPreferences;
    String KEY_EMAIL = "email";
    String SHARED_PREF_NAME = "mypref";
    DBHelper db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        email = findViewById(R.id.loginemail);       // Email EditText field
        password = findViewById(R.id.loginpassword); // Password EditText field

        // Initialize database and shared preferences
        db = new DBHelper(this);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        // Set click listener for login button
        button = findViewById(R.id.loginbtn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String useremail = email.getText().toString();
                String pass = password.getText().toString();

                // Email validation
                if (!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()) {
                    // Show error if email is invalid
                    email.setError("Please enter a valid email address");
                    email.requestFocus();
                    return;
                }

                // Check if email and password fields are empty
                if (useremail.equals("") || pass.equals("")){
                    Toast.makeText(LoginActivity.this,"Please enter all the fields",Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Check if email and password match in the database
                    Boolean checkresult = db.checkemailregpassword(useremail, pass);
                    if (checkresult == true) {
                        // If login is successful
                        Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT)
                                .show();
                        Intent intent = new Intent(LoginActivity.this, HomeNewActivity.class);
                        startActivity(intent);

                        // Save user email to shared preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_EMAIL,useremail);
                        editor.apply();
                    } else {
                        // If login is unsuccessful
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });

        CheckBox showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
        EditText loginPasswordEditText = findViewById(R.id.loginpassword);

        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int inputType = isChecked ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                loginPasswordEditText.setInputType(inputType);
                loginPasswordEditText.setSelection(loginPasswordEditText.getText().length()); // Preserve cursor position
            }
        });


        // Set click listener for "Register Here" text
        textView = findViewById(R.id.registerhere);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open RegisterActivity when clicked
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for "Forgot Password?" text
        textView = findViewById(R.id.forgotpsw);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open ForgotPasswordActivity when clicked
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
