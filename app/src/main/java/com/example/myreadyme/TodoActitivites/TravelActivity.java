package com.example.myreadyme.TodoActitivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myreadyme.Adapter.TodoAdapterTravel;
import com.example.myreadyme.AddNewTasks.AddNewTaskTravel;
import com.example.myreadyme.Database.TravelTodoDatabaseHandler;
import com.example.myreadyme.DialogCloseListener;
import com.example.myreadyme.Models.TravelTodoModel;
import com.example.myreadyme.R;
import com.example.myreadyme.RecyclerItemTouchHelper.RecyclerItemTouchHelper4;

import java.util.Collections;
import java.util.List;

public class TravelActivity extends AppCompatActivity implements DialogCloseListener {

    private TravelTodoDatabaseHandler db;
    private RecyclerView tasksRecyclerView;
    private TodoAdapterTravel tasksAdapter;
    private TextView add;
    private List<TravelTodoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);


        db = new TravelTodoDatabaseHandler(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.taskRecycler);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new TodoAdapterTravel(db, TravelActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);
        add = findViewById(R.id.addTask);

        // get all the work todo task in WorkTodoDatabase
        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        // adept all task to TodoAdapter
        tasksAdapter.setTasks(taskList);

        //Delete and edit todo using RecycleTouchHelper
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper4(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);



        // Implement add new task button
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTaskTravel.newInstance().show(getSupportFragmentManager(), AddNewTaskTravel.TAG);

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



