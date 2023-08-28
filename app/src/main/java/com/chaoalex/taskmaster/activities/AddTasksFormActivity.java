package com.chaoalex.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.chaoalex.taskmaster.MainActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.reactivex.rxjava3.core.Completable;

public class AddTasksFormActivity extends AppCompatActivity {

  CompletableFuture<List<Team>> teamsFuture = null;

  private final String TAG = "AddTaskFormActivity";
  Button saveButton;
  EditText taskTitleEditText;
  EditText taskDescriptionEditText;
  Spinner taskCategorySpinner;
  Spinner taskTeamSpinner;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_tasks);

    teamsFuture = new CompletableFuture<>();

    taskDescriptionEditText = findViewById(R.id.AddTaskDescriptionTaskDescriptionMultiAutoCompleteTextView);
    taskTitleEditText = findViewById(R.id.AddTasksActivityTaskTitleInputTextView);
    taskCategorySpinner = findViewById(R.id.AddTasksActivityStateSpinner);
    taskTeamSpinner = findViewById(R.id.AddTasksActivityTeamSpinner);
    saveButton = findViewById(R.id.AddTasksActivitySaveTaskButton);

    setupTaskCategorySpinner();
    setupTaskTeamSpinner();
    setupSaveButton();
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
              Log.i(TAG, "Read contacts succussfully");
              ArrayList<String> teamNames = new ArrayList<>();
              ArrayList<Team> teams = new ArrayList<>();
              for (Team team : success.getData()) {
                teams.add(team);
                teamNames.add(team.getName());
              }
              teamsFuture.complete(teams);

              runOnUiThread(() -> {
                taskTeamSpinner.setAdapter(new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        teamNames
                ));
              });
            },
            failure -> {
              teamsFuture.complete(null);
              Log.i(TAG, "Did not read contacts successfully!!");
            }
    );
  }

  void setupSaveButton() {
    saveButton.setOnClickListener(v -> {

      Task taskToSave = Task.builder()
              .title(taskTitleEditText.getText().toString())
              .description(taskDescriptionEditText.getText().toString())
              .dateCreated(new Temporal.DateTime(new Date(), 0))
              .taskCategory((TaskCategoryEnum) taskCategorySpinner.getSelectedItem())
              .build();

      Amplify.API.mutate(
              ModelMutation.create(taskToSave),
              successResponse -> Log.i(TAG, "AddTasksFormActivity.setupSaveButton() : made task successfully"),
              failureResponse -> Log.i(TAG, "AddTasksFormActivity.setupSaveButton() : failed with this response " + failureResponse)
      );


      Toast.makeText(AddTasksFormActivity.this, "Task saved!!", Toast.LENGTH_SHORT).show();
    });
  }
}