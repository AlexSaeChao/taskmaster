package com.chaoalex.taskmaster.activities;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskCategoryEnum;

import com.amplifyframework.datastore.generated.model.Team;
import com.chaoalex.taskmaster.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class AddTasksFormActivity extends AppCompatActivity {
  private final String TAG = "AddTasksFormActivity";
  private String s3ImageKey = "";
  ActivityResultLauncher<Intent> activityResultLauncher;
  CompletableFuture<List<Team>> teamsFuture = null;


  ImageView taskImageView;
  EditText taskTitleEditText;
  EditText taskDescriptionEditText;
  Spinner taskCategorySpinner;
  Spinner taskTeamSpinner;
  Button saveButton;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_tasks);

    activityResultLauncher = getImagePickActivityResultsLauncher();
    teamsFuture = new CompletableFuture<>();

    taskImageView = findViewById(R.id.AddTasksFormActivityTaskImageView);
    taskDescriptionEditText = findViewById(R.id.AddTaskDescriptionTaskDescriptionMultiAutoCompleteTextView);
    taskTitleEditText = findViewById(R.id.AddTasksActivityTaskTitleInputTextView);
    taskCategorySpinner = findViewById(R.id.AddTasksActivityStateSpinner);
    taskTeamSpinner = findViewById(R.id.AddTasksFormActivityTeamSpinner);
    saveButton = findViewById(R.id.AddTasksActivitySaveTaskButton);

    setupTaskImageView();
    setupTaskCategorySpinner();
    setupTaskTeamSpinner();
    setupSaveButton();
  }

  void setupTaskImageView() {
    taskImageView.setOnClickListener(v -> {
      launchImageSelectionIntent();
    });
  }

  void setupTaskCategorySpinner() {
    taskCategorySpinner.setAdapter(new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_item,
            TaskCategoryEnum.values()
    ));
  }

  void setupTaskTeamSpinner() {
    Amplify.API.query(
            ModelQuery.list(Team.class),
            success -> {
              Log.i(TAG, "Read teams successfully");
              ArrayList<String> teamNames = new ArrayList<>();
              ArrayList<Team> teams = new ArrayList<>();
              for (Team team : success.getData()) {
                teams.add(team);
                teamNames.add(team.getName());
              }
//              Log.i(TAG, "Team names fetched: " + teamNames);
              teamsFuture.complete(teams);

              runOnUiThread(() -> taskTeamSpinner.setAdapter(new ArrayAdapter<>(
                      this,
                      android.R.layout.simple_spinner_item,
                      teamNames
              )));
            },
            failure -> {
              teamsFuture.complete(null);
              Log.i(TAG, "Failed to read teams");
            }
    );
  }

  void setupSaveButton() {
    saveButton.setOnClickListener(v -> {
      String selectTeamString = taskTeamSpinner.getSelectedItem().toString();

      List<Team> teams = null;
      try {
        teams = teamsFuture.get();
      } catch (InterruptedException ie) {
        Log.e(TAG, "setupSaveButton: InterruptedException while getting teams");
        Thread.currentThread().interrupt();
      } catch (ExecutionException ee) {
        Log.e(TAG, "ExecutionException while getting teams");
      }
      assert teams != null;
      Team selectedTeam = teams.stream().filter(c -> c.getName().equals(selectTeamString)).findAny().orElseThrow(RuntimeException::new);

      String title = taskTitleEditText.getText().toString();
      String description = taskDescriptionEditText.getText().toString();
      if (title.isEmpty() || description.isEmpty()) {
        Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
        return;
      }

      Task taskToSave = Task.builder()
              .title(taskTitleEditText.getText().toString())
              .description(taskDescriptionEditText.getText().toString())
              .dateCreated(new Temporal.DateTime(new Date(), 0))
              .taskCategory((TaskCategoryEnum) taskCategorySpinner.getSelectedItem())
              .team(selectedTeam)
              .taskImageS3key(s3ImageKey)
              .build();

      Amplify.API.mutate(
              ModelMutation.create(taskToSave),
              successResponse -> Log.i(TAG, "AddTasksFormActivity.setupSaveButton() : made task successfully"),
              failureResponse -> Log.i(TAG, "AddTasksFormActivity.setupSaveButton() : failed with this response " + failureResponse)
      );


      Toast.makeText(AddTasksFormActivity.this, "Task saved!!", Toast.LENGTH_SHORT).show();
    });
  }

  void launchImageSelectionIntent() {
    Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
    imageFilePickingIntent.setType("*/*");
    imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});

    activityResultLauncher.launch(imageFilePickingIntent);
  }

  ActivityResultLauncher<Intent> getImagePickActivityResultsLauncher() {
    ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                      @Override
                      public void onActivityResult(ActivityResult result) {
                        Uri pickedImageFileUri = result.getData().getData();
                        try {
                          InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                          String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                          Log.i(TAG, "Succeeded in getting input stream from file on the phone! Filename is: " + pickedImageFilename);
                          uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename, pickedImageFileUri);
                        } catch (FileNotFoundException fnfe) {
                          Log.e(TAG, "Could not get file from file picker! " + fnfe.getMessage(), fnfe);
                        }
                      }
                    }
            );
    return imagePickingActivityResultLauncher;
  }

  @SuppressLint("Range")
  public String getFileNameFromUri(Uri uri) {
    String result = null;
    if (uri.getScheme().equals("content")) {
      Cursor cursor = getContentResolver().query(uri, null, null, null, null);
      try {
        if (cursor != null && cursor.moveToFirst()) {
          result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        }
      } finally {
        cursor.close();
      }
    }
    if (result == null) {
      result = uri.getPath();
      int cut = result.lastIndexOf('/');
      if (cut != -1) {
        result = result.substring(cut + 1);
      }
    }
    return result;
  }

  void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFilename, Uri pickedImageFileUri) {
    Amplify.Storage.uploadInputStream(

            pickedImageFilename,
            pickedImageInputStream,
            success -> {
              Log.i(TAG, "Succeeded in getting file uploaded to S3! The Key is: " + success.getKey());
              s3ImageKey = success.getKey();
              InputStream pickedImageInputStreamCopy = null;
              try {
                pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
              } catch (FileNotFoundException fnfe) {
                Log.e(TAG, "Could not get file from URI... " + fnfe.getMessage(), fnfe);
              }
              taskImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));
            },
            failure -> {
              Log.e(TAG, "Failed to upload file to S3 with filename: " + pickedImageFilename + "with error: " + failure.getMessage());
            }
    );
  }
}