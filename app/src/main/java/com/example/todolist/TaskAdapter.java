// TaskAdapter.java
package com.example.todolist;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends ArrayAdapter<Task> {
    private Handler handler = new Handler();
    private TextView tvExpiryTime;
    private Runnable timerRunnable;

    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvCategory = convertView.findViewById(R.id.tvCategory);
        TextView tvPriority = convertView.findViewById(R.id.tvPriority);
        tvExpiryTime = convertView.findViewById(R.id.tvExpiryTime);

        if (task != null) {
            tvName.setText(task.getTitle());
            tvCategory.setText("Category: " + task.getCategory());
            tvPriority.setText("Priority: " + getPriorityString(task.getPriority()));

            // Check if the task is expired
            if (isTaskExpired(task)) {
                tvExpiryTime.setText("Expired");
            } else {
                // Set up and start the timer for non-expired tasks
                setupTimer(task, tvExpiryTime);
            }
        }

        return convertView;
    }

    // Helper method to convert priority integer to a user-friendly string
    private String getPriorityString(int priority) {
        switch (priority) {
            case 3:
                return "High";
            case 2:
                return "Medium";
            case 1:
                return "Low";
            default:
                return "Unknown";
        }
    }

    // Helper method to check if a task is expired
    private boolean isTaskExpired(Task task) {
        String taskDateTime = task.getDateTime();

        // Parse the task date and time
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date taskDate = dateTimeFormat.parse(taskDateTime);

            // Get the current date and time
            Date currentDate = new Date();

            // Check if the task date and time are in the past
            return taskDate != null && currentDate.after(taskDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // Return false in case of parsing error
        }
    }

    // Helper method to set up and start the timer
    private void setupTimer(Task task, TextView tvExpiryTime) {
        // Parse the task date and time
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date taskDate = dateTimeFormat.parse(task.getDateTime());
            long expiryTimeInMillis = taskDate.getTime();

            // Set up the runnable timer
            timerRunnable = new Runnable() {
                @Override
                public void run() {
                    long currentTimeInMillis = System.currentTimeMillis();
                    long timeDifference = expiryTimeInMillis - currentTimeInMillis;

                    if (timeDifference <= 0) {
                        // Task has expired, stop the timer
                        tvExpiryTime.setText("Expired");
                        handler.removeCallbacks(this);
                    } else {
                        // Calculate days, hours, minutes, and seconds
                        long days = timeDifference / (24 * 60 * 60 * 1000);
                        long remainingHours = (timeDifference / (60 * 60 * 1000)) % 24;
                        long remainingMinutes = (timeDifference / (60 * 1000)) % 60;
                        long remainingSeconds = (timeDifference / 1000) % 60;

                        // Build the remaining time string
                        StringBuilder remainingTime = new StringBuilder();
                        if (days > 0) {
                            remainingTime.append(days);
                            if (days == 1) {
                                remainingTime.append(" day ");
                            } else {
                                remainingTime.append(" days ");
                            }
                        }
                        if (remainingHours > 0) {
                            remainingTime.append(remainingHours);
                            if (remainingHours == 1) {
                                remainingTime.append(" hr ");
                            } else {
                                remainingTime.append(" hrs ");
                            }
                        }
                        if (remainingMinutes > 0) {
                            remainingTime.append(remainingMinutes);
                            if (remainingMinutes == 1) {
                                remainingTime.append(" min ");
                            } else {
                                remainingTime.append(" mins ");
                            }
                        }
                        if (remainingSeconds > 0) {
                            remainingTime.append(remainingSeconds);
                            if (remainingSeconds == 1) {
                                remainingTime.append(" sec ");
                            } else {
                                remainingTime.append(" secs ");
                            }
                        }

                        // Update the countdown timer
                        tvExpiryTime.setText("Expires in: " + remainingTime.toString());
                        handler.postDelayed(this, 1000); // Update every second
                    }
                }
            };

            // Start the timer
            handler.post(timerRunnable);
        } catch (ParseException e) {
            e.printStackTrace();
            tvExpiryTime.setText("Error parsing date"); // Display error message
        }
    }
}
