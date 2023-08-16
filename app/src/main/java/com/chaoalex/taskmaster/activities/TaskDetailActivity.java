package com.chaoalex.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.chaoalex.taskmaster.MainActivity;
import com.chaoalex.taskmaster.R;

public class TaskDetailActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_detail);

    setupTasksNameTextView();
  }

  void setupTasksNameTextView() {
    Intent callingIntent = getIntent();
    String tasksNameString = null;
    if (callingIntent != null) {
      tasksNameString = callingIntent.getStringExtra(MainActivity.TASK_NAME_EXTRA_TAG);
    }

    TextView TasksNameTextView = (TextView)findViewById(R.id.TaskDetailActivityTaskTitleTextView);
    if(tasksNameString != null) {
      TasksNameTextView.setText(tasksNameString);
    } else {
      TasksNameTextView.setText(R.string.no_task_name);
    }
  }
}
