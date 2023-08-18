package com.chaoalex.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chaoalex.taskmaster.MainActivity;
import com.chaoalex.taskmaster.R;
import com.chaoalex.taskmaster.database.TaskMasterDatabase;
import com.chaoalex.taskmaster.models.Task;
import com.chaoalex.taskmaster.models.TaskCategoryEnum;

import java.util.Date;

public class AddTasksFormActivity extends AppCompatActivity {
  TaskMasterDatabase taskMasterDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_tasks);
    taskMasterDatabase = Room.databaseBuilder(
                    getApplicationContext(),
                    TaskMasterDatabase.class,
                    MainActivity.DATABASE_NAME)
            .allowMainThreadQueries()
            .build();

    Spinner taskCategorySpinner = (Spinner) findViewById(R.id.AddTasksActivityStateSpinner);

    setupTaskCategorySpinner(taskCategorySpinner);
    setupSaveButton(taskCategorySpinner);
  }


  void setupTaskCategorySpinner(Spinner taskCategorySpinner) {
//    Spinner taskCategorySpinner = (Spinner) findViewById(R.id.AddTasksActivityStateSpinner);
    taskCategorySpinner.setAdapter(new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_item,
            TaskCategoryEnum.values()
    ));
  }

  void setupSaveButton(Spinner taskCategorySpinner) {
    Button saveButton = (Button) findViewById(R.id.AddTasksActivitySaveTaskButton);
    saveButton.setOnClickListener(v -> {
      Task taskToSave = new Task(
              ((EditText) findViewById(R.id.AddTasksActivityTaskTitleInputTextView)).getText().toString(),
              ((MultiAutoCompleteTextView) findViewById(R.id.AddTaskDescriptionTaskDescriptionMultiAutoCompleteTextView)).getText().toString(),
              new Date(),
              TaskCategoryEnum.fromString(taskCategorySpinner.getSelectedItem().toString())
      );

      taskMasterDatabase.taskDao().insertATask(taskToSave);

      Toast.makeText(AddTasksFormActivity.this, "Task saved!!", Toast.LENGTH_SHORT).show();
    });
  }
}