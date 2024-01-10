package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ADD_TASK = 1;
    private ListView lvTasks;
    private Button btnAddTask;
    private TaskDbHelper dbHelper;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTasks = findViewById(R.id.lvTasks);
        btnAddTask = findViewById(R.id.btnAddTask);

        dbHelper = new TaskDbHelper(this);
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, taskList);
        lvTasks.setAdapter(taskAdapter);

        // Example: Fetch tasks from the database
        fetchTasksFromDatabase();

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddTaskActivity();
            }
        });
    }

    private void fetchTasksFromDatabase() {
        taskList.clear(); // Clear existing tasks

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] projection = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_NAME,
                TaskContract.TaskEntry.COLUMN_CATEGORY,
                TaskContract.TaskEntry.COLUMN_PRIORITY,
                TaskContract.TaskEntry.COLUMN_DESCRIPTION,  // Add this line
                TaskContract.TaskEntry.COLUMN_DATETIME
        };

        Cursor cursor = database.query(
                TaskContract.TaskEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            long taskId = cursor.getLong(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_CATEGORY));
            int priority = cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_PRIORITY));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_DESCRIPTION));

            // Fetch dateTime from the cursor, assuming it's stored in the database
            String dateTime = cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_DATETIME));

            Task task = new Task(name, description, category, priority, dateTime);
            taskList.add(task);
        }

        cursor.close();
        database.close();

        // Notify the adapter that the data set has changed
        taskAdapter.notifyDataSetChanged();
    }

    private void startAddTaskActivity() {
        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_TASK);
    }

    // This method is called when AddTaskActivity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_TASK && resultCode == RESULT_OK) {
            // Fetch tasks from the database after adding a new task
            fetchTasksFromDatabase();
        }
    }
}