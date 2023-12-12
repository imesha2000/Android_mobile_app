package com.example.myreadyme.TodoActitivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myreadyme.Adapter.TodoAdapterWork;
import com.example.myreadyme.AddNewTasks.AddNewTaskWork;
import com.example.myreadyme.Database.WorkTodoDatabaseHandler;
import com.example.myreadyme.DialogCloseListener;
import com.example.myreadyme.Models.WorkTodoModel;
import com.example.myreadyme.R;
import com.example.myreadyme.RecyclerItemTouchHelper.RecyclerItemTouchHelper1;

import java.util.Collections;
import java.util.List;

public class WorkActivity extends AppCompatActivity implements DialogCloseListener {

    private WorkTodoDatabaseHandler db;
    private RecyclerView tasksRecyclerView;
    private TodoAdapterWork tasksAdapter;
    private TextView add;
    private List<WorkTodoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);


        db = new WorkTodoDatabaseHandler(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.taskRecycler);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new TodoAdapterWork(db, WorkActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);
        add = findViewById(R.id.addTask);

        // get all the work todo task in WorkTodoDatabase
        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        // adept all task to TodoAdapter
        tasksAdapter.setTasks(taskList);

        //Delete and edit todo using RecycleTouchHelper
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper1(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);


        // Implement add new task button
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTaskWork.newInstance().show(getSupportFragmentManager(), AddNewTaskWork.TAG);

            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}

