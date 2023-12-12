package com.example.myreadyme.Actitivties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myreadyme.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize UI elements
        // Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_setting);

        // Set click listener for items in the bottom navigation bar
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_setting:
                    return true;
                case R.id.bottom_home:
                    // Navigate to HomeNewActivity
                    startActivity(new Intent(getApplicationContext(), HomeNewActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.bottom_location:
                    // Navigate to Map activity
                    startActivity(new Intent(getApplicationContext(), Map.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

        // Set click listeners for buttons
        button=findViewById(R.id.btnprofile);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open EditProfileActivity
                Intent intent=new Intent(SettingsActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        button=findViewById(R.id.btnAbout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open AboutActivity
                Intent intent=new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        button=findViewById(R.id.btnprivacy);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open PrivacyActivity
                Intent intent=new Intent(SettingsActivity.this, PrivacyActivity.class);
                startActivity(intent);
            }
        });

        button=findViewById(R.id.btnlogout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open LoginActivity
                Intent intent=new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
