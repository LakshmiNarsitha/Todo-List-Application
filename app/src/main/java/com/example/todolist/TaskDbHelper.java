package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "task1.db";
    private static final int DATABASE_VERSION = 2;  // Increment version to trigger onUpgrade

    // SQL statement to create the tasks table
// Update the SQL_CREATE_TASKS_TABLE statement in TaskDbHelper
    private static final String SQL_CREATE_TASKS_TABLE =
            "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " (" +
                    TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TaskContract.TaskEntry.COLUMN_NAME + " TEXT NOT NULL," +
                    TaskContract.TaskEntry.COLUMN_DESCRIPTION + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_CATEGORY + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_PRIORITY + " INTEGER," +
                    TaskContract.TaskEntry.COLUMN_DATETIME + " TEXT)";

    // SQL statement to delete the tasks table
    private static final String SQL_DELETE_TASKS_TABLE =
            "DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME;

    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TASKS_TABLE);
        onCreate(db);
    }
}
