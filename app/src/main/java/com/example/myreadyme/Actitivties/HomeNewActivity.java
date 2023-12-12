package com.example.myreadyme.Actitivties;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myreadyme.Database.DBHelper;
import com.example.myreadyme.R;
import com.example.myreadyme.TodoActitivites.PersonalActivity;
import com.example.myreadyme.TodoActitivites.StudyActivity;
import com.example.myreadyme.TodoActitivites.TravelActivity;
import com.example.myreadyme.TodoActitivites.WorkActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeNewActivity extends AppCompatActivity {

    Button button;
    private DBHelper DB;
    CircleImageView userImage;
    TextView name;

    SharedPreferences sharedPreferences;
    String KEY_EMAIL = "email";
    String SHARED_PREF_NAME = "mypref";

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int lastRotation = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        // Initialize UI elements
        userImage = findViewById(R.id.user); // CircleImageView for user image
        name = findViewById(R.id.name);     // TextView for user name

        // Initialize database and shared preferences
        DB = new DBHelper(getApplicationContext());
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        // Retrieve user email from shared preferences
        String userEmail = sharedPreferences.getString(KEY_EMAIL, null);

        // Retrieve user details from the database
        Cursor cursor = DB.getUserDetails(userEmail);

        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve user details
            String storedUsername = cursor.getString(2);
            byte[] imageByte = cursor.getBlob(3);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
            userImage.setImageBitmap(bitmap);

            // Update the UI with the retrieved user details
            name.setText(storedUsername);
        } else {
            // User not found
            Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }

        // Set up bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    return true;
                case R.id.bottom_location:
                    // Open Map activity
                    startActivity(new Intent(getApplicationContext(), Map.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_setting:
                    // Open Settings activity
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

        // Set click listeners for buttons

        button = findViewById(R.id.btnstudy);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open StudyActivity
                Intent intent = new Intent(HomeNewActivity.this, StudyActivity.class);
                startActivity(intent);
            }
        });

        button = findViewById(R.id.btnwork);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open WorkActivity
                Intent intent = new Intent(HomeNewActivity.this, WorkActivity.class);
                startActivity(intent);
            }
        });

        button = findViewById(R.id.btnpersonal);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open PersonalActivity
                Intent intent = new Intent(HomeNewActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });

        button = findViewById(R.id.btntravel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open TravelActivity
                Intent intent = new Intent(HomeNewActivity.this, TravelActivity.class);
                startActivity(intent);
            }
        });

        // Initialize sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Check if accelerometer sensor is available
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer == null) {
            // Accelerometer sensor is not available on this device
            // Handle the case accordingly
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register the sensor listener
        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the sensor listener to save resources
        sensorManager.unregisterListener(sensorListener);
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];

            if (x < -2) {
                // Rotate left
                // Change the background to the desired image
                getWindow().setBackgroundDrawableResource(R.drawable.background_left);
            } else if (x > 2) {
                // Rotate right
                // Change the background to the desired image
                getWindow().setBackgroundDrawableResource(R.drawable.background_right);
            } else {
                // No rotation
                // Keep the existing background
                getWindow().setBackgroundDrawableResource(R.drawable.background_default);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Ignore this callback for accelerometer sensor
        }
    };

    // Override the onBackPressed method to handle back button press
    @Override
    public void onBackPressed() {
        // Ask the user if they want to exit the app
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Finish the current activity and exit the app
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
