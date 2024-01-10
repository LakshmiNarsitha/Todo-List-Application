package com.example.todolist;

import android.provider.BaseColumns;

// TaskContract.java
import android.provider.BaseColumns;

public final class TaskContract {
    private TaskContract() {
    }

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_DATETIME = "datetime";  // Add this line
    }
}