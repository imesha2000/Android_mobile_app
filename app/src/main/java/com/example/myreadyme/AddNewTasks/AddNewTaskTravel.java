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

import com.example.myreadyme.Database.TravelTodoDatabaseHandler;
import com.example.myreadyme.DialogCloseListener;
import com.example.myreadyme.Models.TravelTodoModel;
import com.example.myreadyme.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class AddNewTaskTravel extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText title, description, date, time;
    private Button newTaskSaveButton;
    private TravelTodoDatabaseHandler db;

    ImageButton calender, timeSelect;

    public static AddNewTaskTravel newInstance() {
        return new AddNewTaskTravel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task_travel, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        title = requireView().findViewById(R.id.addTaskTitle);
        description = requireView().findViewById(R.id.addTaskDescription);
        newTaskSaveButton = getView().findViewById(R.id.addTaskSaveButton);
        date = requireView().findViewById(R.id.taskDate);
        time = requireView().findViewById(R.id.taskTime);

        calender = getView().findViewById(R.id.calender);
        timeSelect = getView().findViewById(R.id.time);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            // Check if it's an update to an existing task
            isUpdate = true;
            String titleTodo = bundle.getString("title");
            String descriptionTodo = bundle.getString("description");
            String todoDate = bundle.getString("date");
            String todoTime = bundle.getString("time");

            // Populate the UI elements with existing task details
            title.setText(titleTodo);
            description.setText(descriptionTodo);
            date.setText(todoDate);
            time.setText(todoTime);

            // Set the color of the save button based on the title length
            assert title != null;
            if (title.length() > 0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }

        db = new TravelTodoDatabaseHandler(getActivity());
        db.openDatabase();

        // Add text change listener to the title EditText
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable/disable save button and change its text color based on title length
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
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = title.getText().toString();
                String description1 = description.getText().toString();
                String date1 = date.getText().toString();
                String Time = time.getText().toString();

                if (finalIsUpdate) {
                    // Update existing task
                    db.updateTask(bundle.getInt("id"), text);
                } else {
                    // Create new task
                    TravelTodoModel task = new TravelTodoModel();
                    task.setTitle(text);
                    task.setTaskDescription(description1);
                    task.setTime(Time);
                    task.setDate(date1);
                    task.setStatus(0);
                    db.insertTask(task);
                }

                dismiss();
            }
        });

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open date picker dialog
                openDatePicker();
            }
        });

        timeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open time picker dialog
                openTimePicker();
            }
        });
    }

    private void openTimePicker() {
        // Get current time
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int mins = calendar.get(Calendar.MINUTE);

        // Create and show time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Format selected time and set it to the time EditText
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

    private void openDatePicker() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Format selected date and set it to the date EditText
                date.setText(String.valueOf(year) + "." + "0" + month + "." + String.valueOf(day));
            }
        }, year, month, day);
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
