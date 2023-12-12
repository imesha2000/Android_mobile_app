package com.example.myreadyme.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myreadyme.AddNewTasks.AddNewTaskStudy;
import com.example.myreadyme.Database.TravelTodoDatabaseHandler;
import com.example.myreadyme.Models.TravelTodoModel;
import com.example.myreadyme.R;
import com.example.myreadyme.TodoActitivites.TravelActivity;

import java.util.List;

public class TodoAdapterTravel extends RecyclerView.Adapter<TodoAdapterTravel.ViewHolder> {

    private List<TravelTodoModel> todoList;
    private TravelTodoDatabaseHandler db;
    private TravelActivity activity;


    public TodoAdapterTravel(TravelTodoDatabaseHandler db, TravelActivity activity) {
        this.db = db;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout_travel, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Open the database
        db.openDatabase();

        // Get the item at the current position
        final TravelTodoModel item = todoList.get(position);

        // Bind the data to the views
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getTaskDescription());
        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());

        // Set a listener for the checkbox
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the status of the item in the database
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<TravelTodoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        TravelTodoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        TravelTodoModel item = todoList.get(position);
        Bundle bundle = new Bundle();

        // Pass the data of the item to the fragment for editing
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTitle());
        bundle.putString("description", item.getTaskDescription());
        bundle.putString("date", item.getDate());
        bundle.putString("time", item.getTime());

        AddNewTaskStudy fragment = new AddNewTaskStudy();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTaskStudy.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;
        TextView title, description, date, time;

        ViewHolder(View view) {
            super(view);
            // Initialize the views
            task = view.findViewById(R.id.todoCheckBoxWork2);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            date = view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);
        }
    }

}
