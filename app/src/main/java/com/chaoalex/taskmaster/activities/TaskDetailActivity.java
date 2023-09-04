package com.chaoalex.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.chaoalex.taskmaster.MainActivity;
import com.chaoalex.taskmaster.R;

import java.io.File;

public class TaskDetailActivity extends AppCompatActivity {
  private static final String TAG = "TaskDetailActivity";
  Intent callingIntent;
  Task currentTask;
  String s3ImageKey;

  ImageView taskImageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_detail);

    callingIntent = getIntent();

    taskImageView = findViewById(R.id.TaskDetailActivityImageView);

    setupTasksImageView();
    setupTasksNameTextView();
    setupTasksDescriptionTextView();
  }

  void setupTasksImageView() {
    String taskId = "";
    if (callingIntent != null) {
      taskId = callingIntent.getStringExtra(MainActivity.TASK_ID_EXTRA_TAG);
    }

    if (!taskId.equals("")) {
      String finalTaskId = taskId;
      Amplify.API.query(
              ModelQuery.get(Task.class, taskId),
              success -> {
                if (success.getData() != null) {  // Add this null check
                  Log.i(TAG, "successfully found task with id: " + success.getData().getId());
                  currentTask = success.getData();
                  populateImageView();
                } else {
                  Log.e(TAG, "No data received for the task id: " + finalTaskId);
                }
              },
              failure -> Log.i(TAG, "Failure to query task with from: " + failure.getMessage())
      );
    }
  }

  void populateImageView() {
    // truncate folder name from tasks s3key
    if(currentTask.getTaskImageS3key() != null) {
      int cut = currentTask.getTaskImageS3key().lastIndexOf('/');
      if(cut != -1) {
        s3ImageKey = currentTask.getTaskImageS3key().substring(cut + 1);
      }
    }

    if(s3ImageKey != null && !s3ImageKey.isEmpty()) {
      Amplify.Storage.downloadFile(
              s3ImageKey,
              new File(getApplication().getFilesDir(), s3ImageKey),
              success -> runOnUiThread(() -> taskImageView.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()))),
              failure -> {
                Log.e(TAG, "Unable to get image from S3 for the task-S3 key: " + s3ImageKey + " error: " + failure.getMessage());
                runOnUiThread(() -> {
                  // Optionally provide feedback to the user, for example:
                   Toast.makeText(TaskDetailActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                });
              }
      );
    }
  }

  void setupTasksNameTextView() {
    String tasksNameString = null;

    if (callingIntent != null) {
      tasksNameString = callingIntent.getStringExtra(MainActivity.TASK_NAME_EXTRA_TAG);
    }

    TextView tasksNameTextView = findViewById(R.id.TaskDetailActivityTaskTitleTextView);
    if (tasksNameString != null) {
      tasksNameTextView.setText(tasksNameString);
    } else {
      tasksNameTextView.setText(R.string.no_task_name);
    }
  }

  void setupTasksDescriptionTextView() {
    String tasksDescriptionString = null;

    if (callingIntent != null) {
      tasksDescriptionString = callingIntent.getStringExtra(MainActivity.TASK_DESCRIPTION_EXTRA_TAG);
    }

    TextView tasksDescriptionTextView = findViewById(R.id.TaskDetailActivityTaskDescriptionTextView);
    if (tasksDescriptionString != null) {
      tasksDescriptionTextView.setText(tasksDescriptionString);
    } else {
      tasksDescriptionTextView.setText(R.string.no_task_description);
    }
  }
}
