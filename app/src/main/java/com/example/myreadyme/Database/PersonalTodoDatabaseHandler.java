package com.example.myreadyme.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myreadyme.Models.PersonalTodoModel;
import com.example.myreadyme.Models.WorkTodoModel;

import java.util.ArrayList;
import java.util.List;

public class PersonalTodoDatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "PersonalToDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID_P = "id";
    private static final String TITLE_P = "title";
    private static final String DESCRIPTION_P= "description";
    private static final String STATUS_P = "status";
    private static final String DATE_P = "date";
    private static final String TIME_P = "time";

    // SQL statement to create the todo table
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID_P + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE_P + " TEXT, "+DESCRIPTION_P + " TEXT, "
            + STATUS_P + " INTEGER," + DATE_P + " String," + TIME_P + " String)";

    private SQLiteDatabase db;

    public PersonalTodoDatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the SQL statement to create the todo table
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing todo table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create the todo table again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(PersonalTodoModel task){
        // Prepare the task data to be inserted into the database
        ContentValues cv = new ContentValues();
        cv.put(TITLE_P, task.getTitle());
        cv.put(DESCRIPTION_P, task.getTaskDescription());
        cv.put(DATE_P,task.getDate());
        cv.put(TIME_P,task.getTime());
        cv.put(STATUS_P, 0);

        // Insert the task into the todo table
        db.insert(TODO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<PersonalTodoModel> getAllTasks(){
        List<PersonalTodoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            // Query all the tasks from the todo table
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);

            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        // Create a PersonalTodoModel object and populate it with task data
                        PersonalTodoModel task = new PersonalTodoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID_P)));
                        task.setTitle(cur.getString(cur.getColumnIndex(TITLE_P)));
                        task.setTaskDescription(cur.getString(cur.getColumnIndex(DESCRIPTION_P)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS_P)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE_P)));
                        task.setTime(cur.getString(cur.getColumnIndex(TIME_P)));

                        // Add the task to the task list
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            // End the transaction and close the cursor
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        // Return the list of tasks
        return taskList;
    }

    public void updateStatus(int id, int status){
        // Prepare the status value to be updated for the given task ID
        ContentValues cv = new ContentValues();
        cv.put(STATUS_P, status);

        // Update the status of the task in the todo table
        db.update(TODO_TABLE, cv, ID_P + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        // Prepare the task value to be updated for the given task ID
        ContentValues cv = new ContentValues();
        cv.put(TITLE_P, task);

        // Update the task in the todo table
        db.update(TODO_TABLE, cv, ID_P + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateDescription(int id, String description) {
        // Prepare the description value to be updated for the given task ID
        ContentValues cv = new ContentValues();
        cv.put(TITLE_P, description);

        // Update the description of the task in the todo table
        db.update(TODO_TABLE, cv, ID_P + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateDate(int id, String date) {
        // Prepare the date value to be updated for the given task ID
        ContentValues cv = new ContentValues();
        cv.put(DATE_P, date);

        // Update the date of the task in the todo table
        db.update(TODO_TABLE, cv, ID_P + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTime(int id, String time) {
        // Prepare the time value to be updated for the given task ID
        ContentValues cv = new ContentValues();
        cv.put(DATE_P, time);

        // Update the time of the task in the todo table
        db.update(TODO_TABLE, cv, ID_P + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        // Delete the task with the given ID from the todo table
        db.delete(TODO_TABLE, ID_P + "= ?", new String[] {String.valueOf(id)});
    }

}
