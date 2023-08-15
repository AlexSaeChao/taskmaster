package com.chaoalex.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.chaoalex.taskmaster.MainActivity;
import com.chaoalex.taskmaster.R;

public class AddTasksFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        Button addTasksButton = findViewById(R.id.AddTasksActivityAddTaskButton);
        addTasksButton.setOnClickListener(v -> {
            System.out.println("Add tasks button was pressed. Submitted!");

            ((TextView)findViewById(R.id.AddTasksActivitySubmitTextView)).setText("Submitted!");

        });
    }
}