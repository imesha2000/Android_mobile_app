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

import com.example.myreadyme.AddNewTasks.AddNewTaskPersonal;
import com.example.myreadyme.Database.PersonalTodoDatabaseHandler;
import com.example.myreadyme.Models.PersonalTodoModel;
import com.example.myreadyme.R;
import com.example.myreadyme.TodoActitivites.PersonalActivity;

import java.util.List;

public class TodoAdapterPersonal extends RecyclerView.Adapter<TodoAdapterPersonal.ViewHolder> {

    private List<PersonalTodoModel> todoList;
    private PersonalTodoDatabaseHandler db;
    private PersonalActivity activity;

    public TodoAdapterPersonal(PersonalTodoDatabaseHandler db, PersonalActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout_personal, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Open the database
        db.openDatabase();

        // Get the item at the given position
        final PersonalTodoModel item = todoList.get(position);

        // Set the checkbox state, title, description, date, and time for the item
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getTaskDescription());
        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());

        // Set a listener to update the task status when the checkbox is checked or unchecked
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Update the task status to checked (1)
                    db.updateStatus(item.getId(), 1);
                } else {
                    // Update the task status to unchecked (0)
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

    public void setTasks(List<PersonalTodoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        // Get the item to be deleted
        PersonalTodoModel item = todoList.get(position);

        // Delete the item from the database
        db.deleteTask(item.getId());

        // Remove the item from the list
        todoList.remove(position);

        // Notify the adapter that the item has been removed
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        // Get the item to be edited
        PersonalTodoModel item = todoList.get(position);

        // Create a bundle to pass the item data to the edit fragment/activity
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTitle());
        bundle.putString("description", item.getTaskDescription());
        bundle.putString("date", item.getDate());
        bundle.putString("time", item.getTime());

        // Create the edit fragment/activity and set the bundle as arguments
        AddNewTaskPersonal fragment = new AddNewTaskPersonal();
        fragment.setArguments(bundle);

        // Show the edit fragment/activity
        fragment.show(activity.getSupportFragmentManager(), AddNewTaskPersonal.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;
        TextView title, description, date, time;

        ViewHolder(View view) {
            super(view);
            // Initialize the views for each item in the RecyclerView
            task = view.findViewById(R.id.todoCheckBoxWork2);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            date = view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);
        }
    }
}
