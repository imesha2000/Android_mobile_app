package com.example.myreadyme.AddNewTasks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.myreadyme.Database.PersonalTodoDatabaseHandler;
import com.example.myreadyme.Database.WorkTodoDatabaseHandler;
import com.example.myreadyme.DialogCloseListener;
import com.example.myreadyme.Models.PersonalTodoModel;
import com.example.myreadyme.Models.WorkTodoModel;
import com.example.myreadyme.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class AddNewTaskPersonal extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText title, description, date, time;
    private Button newTaskSaveButton;
    private PersonalTodoDatabaseHandler db;

    ImageButton calender, timeSelect;

    // Static factory method to create a new instance of the dialog fragment
    public static AddNewTaskPersonal newInstance() {
        return new AddNewTaskPersonal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflating the layout for the dialog fragment
        View view = inflater.inflate(R.layout.new_task_personal, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initializing UI elements
        title = requireView().findViewById(R.id.addTaskTitle);
        description = requireView().findViewById(R.id.addTaskDescription);
        newTaskSaveButton = getView().findViewById(R.id.addTaskSaveButton);
        date = requireView().findViewById(R.id.taskDate);
        time = requireView().findViewById(R.id.taskTime);

        calender = getView().findViewById(R.id.calender);
        timeSelect = getView().findViewById(R.id.time);

        boolean isUpdate = false;

        // Checking if the dialog fragment is in update mode and populating fields with task data if available
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String titleTodo = bundle.getString("title");
            String descriptionTodo = bundle.getString("description");
            String todoDate = bundle.getString("date");
            String todoTime = bundle.getString("time");
            title.setText(titleTodo);
            description.setText(descriptionTodo);
            date.setText(todoDate);
            time.setText(todoTime);
            assert title != null;
            if (title.length() > 0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }

        // Creating an instance of the database handler
        db = new PersonalTodoDatabaseHandler(getActivity());
        db.openDatabase();

        // Adding a TextWatcher to the title EditText to enable/disable the save button based on its content
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                } else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        // Click listener for the save button
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = title.getText().toString();
                String description1 = description.getText().toString();
                String date1 = date.getText().toString();
                String Time = time.getText().toString();

                if (finalIsUpdate) {
                    // Updating an existing task in the database
                    db.updateTask(bundle.getInt("id"), text);
                } else {
                    // Inserting a new task into the database
                    PersonalTodoModel task = new PersonalTodoModel();
                    task.setTitle(text);
                    task.setTaskDescription(description1);
                    task.setTime(Time);
                    task.setDate(date1);
                    task.setStatus(0);
                    db.insertTask(task);
                }

                dismiss(); // Dismissing the dialog fragment
            }
        });

        // Click listener for the calendar button
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(); // Open date picker dialog
            }
        });

        // Click listener for the time button
        timeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(); // Open time picker dialog
            }
        });
    }

    // Method to open the time picker dialog
    private void openTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int mins = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                cal.setTimeZone(TimeZone.getDefault());
                SimpleDateFormat format = new SimpleDateFormat("k:mm a");
                String time1 = format.format(cal.getTime());
                time.setText(time1);
            }
        }, hours, mins, false);

        timePickerDialog.show();
    }

    // Method to open the date picker dialog
    private void openDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Showing the picked value in the textView
                date.setText(String.valueOf(year) + "." + "0" + month + "." + String.valueOf(day));
            }
        }, 2023, 01, 20);

        datePickerDialog.show();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }
    }
}



