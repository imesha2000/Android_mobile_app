package com.example.myreadyme.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myreadyme.Models.PersonalTodoModel;
import com.example.myreadyme.Models.StudyTodoModel;

import java.util.ArrayList;
import java.util.List;

public class StudyTodoDatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "StudyToDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID_S = "id";
    private static final String TITLE_S = "title";
    private static final String DESCRIPTION_S = "description";
    private static final String STATUS_S = "status";
    private static final String DATE_S = "date";
    private static final String TIME_S = "time";

    // SQL statement to create the todo table
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID_S + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE_S + " TEXT, "+ DESCRIPTION_S + " TEXT, "
            + STATUS_S + " INTEGER," + DATE_S + " String," + TIME_S + " String)";

    private SQLiteDatabase db;

    public StudyTodoDatabaseHandler(Context context) {
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

    public void insertTask(StudyTodoModel task){
        // Prepare the task data to be inserted into the database
        ContentValues cv = new ContentValues();
        cv.put(TITLE_S, task.getTitle());
        cv.put(DESCRIPTION_S, task.getTaskDescription());
        cv.put(DATE_S,task.getDate());
        cv.put(TIME_S,task.getTime());
        cv.put(STATUS_S, 0);

        // Insert the task into the todo table
        db.insert(TODO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<StudyTodoModel> getAllTasks(){
        List<StudyTodoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            // Query all the tasks from the todo table
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);

            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        // Create a StudyTodoModel object and populate it with task data
                        StudyTodoModel task = new StudyTodoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID_S)));
                        task.setTitle(cur.getString(cur.getColumnIndex(TITLE_S)));
                        task.setTaskDescription(cur.getString(cur.getColumnIndex(DESCRIPTION_S)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS_S)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE_S)));
                        task.setTime(cur.getString(cur.getColumnIndex(TIME_S)));

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
        cv.put(STATUS_S, status);

        // Update the status of the task in the todo table
        db.update(TODO_TABLE, cv, ID_S + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        // Prepare the task value to be updated for the given task ID
        ContentValues cv = new ContentValues();
        cv.put(TITLE_S, task);

        // Update the task in the todo table
        db.update(TODO_TABLE, cv, ID_S + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateDescription(int id, String description) {
        // Prepare the description value to be updated for the given task ID
        ContentValues cv = new ContentValues();
        cv.put(TITLE_S, description);

        // Update the description of the task in the todo table
        db.update(TODO_TABLE, cv, ID_S + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateDate(int id, String date) {
        // Prepare the date value to be updated for the given task ID
        ContentValues cv = new ContentValues();
        cv.put(DATE_S, date);

        // Update the date of the task in the todo table
        db.update(TODO_TABLE, cv, ID_S + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTime(int id, String time) {
        // Prepare the time value to be updated for the given task ID
        ContentValues cv = new ContentValues();
        cv.put(DATE_S, time);

        // Update the time of the task in the todo table
        db.update(TODO_TABLE, cv, ID_S + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        // Delete the task with the given ID from the todo table
        db.delete(TODO_TABLE, ID_S + "= ?", new String[] {String.valueOf(id)});
    }

}
