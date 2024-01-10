package com.example.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Task.java
public class Task {
    private String title;
    private String description;
    private String category;
    private int priority;
    private String dateTime;

    public static String getCurrentDateTimeWithSeconds() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Task(String title, String description, String category, int priority, String dateTime) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.dateTime = dateTime;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public int getPriority() {
        return priority;
    }

    // Setter methods
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
