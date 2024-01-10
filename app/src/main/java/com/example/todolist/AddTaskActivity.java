package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {
    private EditText etTaskTitle;
    private EditText etTaskDescription;
    private Spinner spinnerCategory;
    private Spinner spinnerPriority;
    private EditText etTaskDateTime;
    private Button btnAdd;
    private TaskDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTaskTitle = findViewById(R.id.etTaskTitle);
        etTaskDescription = findViewById(R.id.etTaskDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        etTaskDateTime = findViewById(R.id.etTaskDateTime);
        btnAdd = findViewById(R.id.btnAdd);

        dbHelper = new TaskDbHelper(this);

        // Set up the category spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.category_array,
                android.R.layout.simple_spinner_item
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Set up the priority spinner
        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.priority_array,
                android.R.layout.simple_spinner_item
        );
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);

        // Set up date and time picker
        etTaskDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });
    }

    private void showDateTimePicker() {
        // Get the current date and time
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Show date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Set the selected date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                // Show time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Set the selected time
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minute);

                        // Combine date and time
                        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        String formattedDateTime = dateTimeFormat.format(selectedTime.getTime());

                        // Update the EditText with the selected date and time
                        etTaskDateTime.setText(formattedDateTime);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void addTask() {
        String taskTitle = etTaskTitle.getText().toString();
        String taskDescription = etTaskDescription.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String priorityString = spinnerPriority.getSelectedItem().toString();
        int priority = getPriorityValue(priorityString);

        // Get the current date and time with seconds
        // Get the selected date and time from EditText
        String selectedDateTime = etTaskDateTime.getText().toString();

// Create a new task
        Task newTask = new Task(taskTitle, taskDescription, category, priority, selectedDateTime);

        // Save the task to the database
        long taskId = saveTaskToDb(newTask);

        if (taskId != -1) {
            // Successfully added to the database
            // Pass the result back to MainActivity
            setResult(RESULT_OK);
            // Close the activity
            finish();
        } else {
            // Failed to add to the database
            // Handle the error
            Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show();
            Log.e("AddTaskActivity", "Failed to add task to the database");
        }
    }

    private long saveTaskToDb(Task task) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME, task.getTitle());
        values.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TaskContract.TaskEntry.COLUMN_CATEGORY, task.getCategory());
        values.put(TaskContract.TaskEntry.COLUMN_PRIORITY, task.getPriority());
        values.put(TaskContract.TaskEntry.COLUMN_DATETIME, task.getDateTime());

        long newRowId = database.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
        database.close();
        return newRowId;
    }

    private int getPriorityValue(String priorityString) {
        switch (priorityString) {
            case "High":
                return 3;
            case "Medium":
                return 2;
            case "Low":
                return 1;
            default:
                return 0;
        }
    }
}
