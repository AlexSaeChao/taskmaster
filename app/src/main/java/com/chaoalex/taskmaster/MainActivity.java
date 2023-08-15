package com.chaoalex.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.chaoalex.taskmaster.activities.AddTasksFormActivity;
import com.chaoalex.taskmaster.activities.AllTasksActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addTasksButton = findViewById(R.id.MainActivityAddTasksButton);
        addTasksButton.setOnClickListener(v -> {

            System.out.println("Add tasks button was pressed.");

            Intent goToAddTasksFormIntent = new Intent(MainActivity.this, AddTasksFormActivity.class);
            startActivity(goToAddTasksFormIntent);

        });

        Button allTasksButton = findViewById(R.id.MainActivityShowAllTasksButton);
        allTasksButton.setOnClickListener(v -> {

            System.out.println("All tasks button was pressed.");

            Intent goToAllTasksFormIntent = new Intent(MainActivity.this, AllTasksActivity.class);
            startActivity(goToAllTasksFormIntent);
        });
    }
}
