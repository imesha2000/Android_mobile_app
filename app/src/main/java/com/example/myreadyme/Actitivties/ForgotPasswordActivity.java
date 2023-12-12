package com.example.myreadyme.Actitivties;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myreadyme.Database.DBHelper;
import com.example.myreadyme.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    Button button;
    EditText fpemail;
    DBHelper db;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        fpemail=findViewById(R.id.fpemail);
        db=new DBHelper(this);


        button=findViewById(R.id.submitbtn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=fpemail.getText().toString();
                Boolean checkresult=db.checkemail(email);
                if (checkresult==true){
                    Intent intent=new Intent(ForgotPasswordActivity.this,
                            ResetPasswordActivity.class);
                    intent.putExtra("email",email);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ForgotPasswordActivity.this,"user does not exists",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}