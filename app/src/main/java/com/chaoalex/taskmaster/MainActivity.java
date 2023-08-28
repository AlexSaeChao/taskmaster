package com.chaoalex.taskmaster;

import static com.chaoalex.taskmaster.activities.SettingsActivity.USER_NICKNAME_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.chaoalex.taskmaster.activities.AddTasksFormActivity;
import com.chaoalex.taskmaster.activities.AllTasksActivity;
import com.chaoalex.taskmaster.activities.SettingsActivity;
import com.chaoalex.taskmaster.adapters.TaskListRecyclerViewAdapter;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskCategoryEnum;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  private final String TAG = "MainActivity";
  public static final String TASK_NAME_EXTRA_TAG = "tasksName";
  public static final String TASK_DESCRIPTION_EXTRA_TAG = "tasksDescription";
  SharedPreferences preferences;
  List<Task> tasks = new ArrayList<>();
  TaskListRecyclerViewAdapter adapter;
  String selectedTeam;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);

//*** TO CREATE TEAMS ***
//    createTeams();
    setupSettingsButton();
    setupAddTasksButton();
    setupAllTasksButton();
    updateTaskListFromDatabase();
    selectedTeam = preferences.getString("selected_team", null);
    setupRecyclerView();
  }

  @Override
  protected void onResume() {
    super.onResume();

    setupUsernameTextView();
    selectedTeam = preferences.getString("selected_team", null);
    updateTaskListFromDatabase();
  }

//*** TO CREATE TEAMS ***

//  public void createTeams() {
//    String[] teamNames = {"Team A", "Team B", "Team C"};
//
//    for (String teamName : teamNames) {
//      Team team = Team.builder()
//              .name(teamName)
//              .build();
//
//      Amplify.API.mutate(
//              ModelMutation.create(team),
//              response -> Log.i("MainActivity.createTeams", "Added Team with id: " + response.getData().getId()),
//              error -> Log.e("MainActivity.createTeams", "Create team failed", error)
//      );
//    }
//  }

  void setupSettingsButton() {
    ImageView settingsButton = findViewById(R.id.MainActivitySettingsButton);
    settingsButton.setOnClickListener(v -> {
      Intent goToSettingsActivityIntent = new Intent(MainActivity.this, SettingsActivity.class);
      startActivity(goToSettingsActivityIntent);
    });
  }

  void setupUsernameTextView() {
    String userNickname = preferences.getString(USER_NICKNAME_TAG, "No Nickname");
    ((TextView) findViewById(R.id.MainActivityUserNicknameTextView)).setText(userNickname);
  }

  void setupAddTasksButton() {
    Button addTasksButton = findViewById(R.id.MainActivityMoveToAddTasksButton);
    addTasksButton.setOnClickListener(v -> {
      System.out.println("Add tasks button was pressed.");
      Intent goToAddTasksFormIntent = new Intent(MainActivity.this, AddTasksFormActivity.class);
      startActivity(goToAddTasksFormIntent);
    });
  }

  void setupAllTasksButton() {
    Button allTasksButton = findViewById(R.id.MainActivityShowAllTasksButton);
    allTasksButton.setOnClickListener(v -> {
      System.out.println("All tasks button was pressed.");
      Intent goToAllTasksFormIntent = new Intent(MainActivity.this, AllTasksActivity.class);
      startActivity(goToAllTasksFormIntent);
    });
  }

  void updateTaskListFromDatabase() {
    // todo: make a dynamoDB GRAPHQL call
    Amplify.API.query(
            ModelQuery.list(Task.class),
            success -> {
              Log.i(TAG, "Read tasks successfully!");
              tasks.clear();
              for (Task databaseTask : success.getData()) {
                if (selectedTeam == null || databaseTask.getTeam().getName().equals(selectedTeam)) {
                  tasks.add(databaseTask);
                }
              }
              runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
              });
            },
            failure -> Log.i(TAG, "Did not read tasks successfully.")
    );
  }

  void setupRecyclerView() {
    RecyclerView TaskListsRecyclerView = (RecyclerView) findViewById(R.id.taskFragmentTextView);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    TaskListsRecyclerView.setLayoutManager(layoutManager);

    int spaceInPixels = getResources().getDimensionPixelSize(R.dimen.task_fragment_spacing);
    TaskListsRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = spaceInPixels;

        if (parent.getChildAdapterPosition(view) == tasks.size() - 1) {
          outRect.bottom = 0;
        }
      }
    });

    adapter = new TaskListRecyclerViewAdapter(tasks, this);

    TaskListsRecyclerView.setAdapter(adapter);
  }
}
