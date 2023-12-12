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

import com.example.myreadyme.Adapter.TodoAdapterPersonal;
import com.example.myreadyme.AddNewTasks.AddNewTaskPersonal;
import com.example.myreadyme.Database.PersonalTodoDatabaseHandler;
import com.example.myreadyme.DialogCloseListener;
import com.example.myreadyme.Models.PersonalTodoModel;
import com.example.myreadyme.R;
import com.example.myreadyme.RecyclerItemTouchHelper.RecyclerItemTouchHelper2;

import java.util.Collections;
import java.util.List;

public class PersonalActivity extends AppCompatActivity implements DialogCloseListener {

    private PersonalTodoDatabaseHandler db;
    private RecyclerView tasksRecyclerView;
    private TodoAdapterPersonal tasksAdapter;
    private TextView add;
    private List<PersonalTodoModel> taskList;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);


        db = new PersonalTodoDatabaseHandler(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.taskRecycler);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new TodoAdapterPersonal(db, PersonalActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);
        add = findViewById(R.id.addTask);

        // get all the work todo task in WorkTodoDatabase
        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        // adept all task to TodoAdapter
        tasksAdapter.setTasks(taskList);

        //Delete and edit todo using RecycleTouchHelper
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper2(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);



        // Implement add new task button
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTaskPersonal.newInstance().show(getSupportFragmentManager(), AddNewTaskPersonal.TAG);

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

