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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaoalex.taskmaster.activities.AddTasksFormActivity;
import com.chaoalex.taskmaster.activities.AllTasksActivity;
import com.chaoalex.taskmaster.activities.SettingsActivity;
import com.chaoalex.taskmaster.adapters.TaskListRecyclerViewAdapter;
import com.chaoalex.taskmaster.models.Task;
import com.chaoalex.taskmaster.models.TaskCategoryEnum;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  private final String TAG = "MainActivity";
  public static final String TASK_NAME_EXTRA_TAG = "tasksName";
  public static final String TASK_DESCRIPTION_EXTRA_TAG = "tasksDescription";
  SharedPreferences preferences;
  List<Task> tasks = new ArrayList<>();


  TaskListRecyclerViewAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);


    setupSettingsButton();
    setupAddTasksButton();
    setupAllTasksButton();
    createTaskInstants();
    setupRecyclerView();
  }


  @Override
  protected void onResume() {
    super.onResume();

    setupUsernameTextView();
    updateTaskListFromDatabase();
  }


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

  void setupRecyclerView() {
    RecyclerView TaskListsRecyclerView = (RecyclerView) findViewById(R.id.taskFragmentTextView);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    TaskListsRecyclerView.setLayoutManager(layoutManager);
//    TaskListRecyclerViewAdapter adapter = new TaskListRecyclerViewAdapter();

//    TaskListRecyclerViewAdapter adapter = new TaskListRecyclerViewAdapter(tasks);
//    TaskListsRecyclerView.setAdapter(adapter);

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

  void updateTaskListFromDatabase() {
    // todo: make a dynamoDB GRAPHQL call
//    tasks.clear();
//    tasks.addAll(taskMasterDatabase.taskdao().findall());
    adapter.notifyDataSetChanged();
  }


  void createTaskInstants() {
    //temp task
    Task taskOne = new Task("Test", "Test Description", new java.util.Date(), TaskCategoryEnum.ASSIGNED);
    Task taskTwo = new Task("Test 2", "Test Description 2", new java.util.Date(), TaskCategoryEnum.NEW);
    tasks.add(taskOne);
    tasks.add(taskTwo);
  }

}
