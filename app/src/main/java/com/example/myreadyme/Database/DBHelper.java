package com.example.myreadyme.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    private Context context;
    // Store image as a byte array
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;

    // Constructor method for DBHelper class
    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
        this.context= context;
    }

    @Override
    //create the user table
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table users(email TEXT primary key,regpassword TEXT,username TEXT, userImage BLOB)");
    }

    @Override
    //drop users table
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists users");
    }
    //define the parameters
    public Boolean insertData(String name, String regpassword, String email, Bitmap image) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try{

            //Store image
            byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            imageInBytes = byteArrayOutputStream.toByteArray();

            contentValues.put("email", email);
            contentValues.put("username", name);
            contentValues.put("regpassword", regpassword);
            contentValues.put("userImage", imageInBytes);
        }catch(Exception e){
            Toast.makeText(context, "Please choose a image", Toast.LENGTH_SHORT).show();
        }

        long result = sqLiteDatabase.insert("users", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Boolean checkemail(String email) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from users where email = ?", new String[]{email});
        if (cursor.getCount() > 0)
            return true; // Email found in the database
        else
            return false; // Email not found in the database
    }

    public Boolean checkemailregpassword(String email, String regpassword) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from users where email = ? and regpassword =?", new String[]{email, regpassword});
        if (cursor.getCount() > 0) {
            return true; // Email and registration password match found in the database
        } else {
            return false; // Email and registration password match not found in the database
        }
    }

    public Cursor getUserDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from users where email = ?";
        String[] selectionArgs = {email};
        return db.rawQuery(query, selectionArgs); // Retrieve user details from the database based on the provided email
    }



    // This method updates the password for a user
    public Boolean updatepassword(String password, String email) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("regpassword", password);
        long result = sqLiteDatabase.update("users", contentValues, "email =?", new String[]{email});
        if (result > 0)
            return true;
        else
            return false;
    }
}
