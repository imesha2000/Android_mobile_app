package com.example.myreadyme.TodoActitivites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myreadyme.Adapter.TodoAdapterStudy;
import com.example.myreadyme.AddNewTasks.AddNewTaskStudy;
import com.example.myreadyme.Database.StudyTodoDatabaseHandler;
import com.example.myreadyme.DialogCloseListener;
import com.example.myreadyme.Models.StudyTodoModel;
import com.example.myreadyme.R;
import com.example.myreadyme.RecyclerItemTouchHelper.RecyclerItemTouchHelper3;

import java.util.Collections;
import java.util.List;

public class StudyActivity extends AppCompatActivity implements DialogCloseListener {

    private StudyTodoDatabaseHandler db;
    private RecyclerView tasksRecyclerView;
    private TodoAdapterStudy tasksAdapter;
    private TextView add;
    private List<StudyTodoModel> taskList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);


        db = new StudyTodoDatabaseHandler(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.taskRecycler);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new TodoAdapterStudy(db, StudyActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);
        add = findViewById(R.id.addTask);

        // get all the work todo task in WorkTodoDatabase
        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        // adept all task to TodoAdapter
        tasksAdapter.setTasks(taskList);

        //Delete and edit todo using RecycleTouchHelper
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper3(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);



        // Implement add new task button
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTaskStudy.newInstance().show(getSupportFragmentManager(), AddNewTaskStudy.TAG);

            }
        });
    }


    public void handleDialogClose( @NonNull DialogInterface dialog) {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}


