package com.example.myreadyme.Actitivties;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myreadyme.Database.DBHelper;
import com.example.myreadyme.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {


    TextView name,email,password;
    CircleImageView user;

    DBHelper db;

    SharedPreferences sharedPreferences;
    String KEY_EMAIL = "email";
    String SHARED_PREF_NAME = "mypref";

    Button restPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        user = findViewById(R.id.user);
        restPassword = findViewById(R.id.pRest);

        db = new DBHelper(getApplicationContext());

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String userEmail = sharedPreferences.getString(KEY_EMAIL,null);

        // Retrieve user details from the database
        Cursor cursor = db.getUserDetails(userEmail);

        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve user details
            String storedUsername = cursor.getString(2);
            String password1= cursor.getString(1);
            String email1 = cursor.getString(0);
            byte[] imageByte = cursor.getBlob(3);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);
            user.setImageBitmap(bitmap);

            // Update the UI with the retrieved user details
            name.setText(storedUsername);
            email.setText(email1);
            password.setText(password1);
        } else {
            // User not found
            Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }



        //Navigation Bar adding
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_setting);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_setting:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), HomeNewActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;


                case R.id.bottom_location:
                    startActivity(new Intent(getApplicationContext(), Map.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

        restPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class));
            }
        });

    }
}