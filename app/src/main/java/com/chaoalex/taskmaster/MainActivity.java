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
import com.chaoalex.taskmaster.models.Type;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  public static final String TASK_NAME_EXTRA_TAG = "tasksName";
  SharedPreferences preferences;

  List<Task> tasks = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);


    setupSettingsButton();
//    setupTask1Button();
//    setupTask2Button();
//    setupTask3Button();
    setupAddTasksButton();
    setupAllTasksButton();
    createTaskInstances();
    setupRecyclerView();
  }


  @Override
  protected void onResume() {
    super.onResume();

    setupUsernameTextView();
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

//  void setupTask1Button() {
//    Button Task1Button = findViewById(R.id.MainActivityTask1Button);
//    Task1Button.setOnClickListener(v -> {
//      String tasksName = ((Button)findViewById(R.id.MainActivityTask1Button)).getText().toString();
//      Intent goToTaskDetailIntent = new Intent(MainActivity.this, TaskDetailActivity.class);
//      goToTaskDetailIntent.putExtra(TASK_NAME_EXTRA_TAG, tasksName);
//      startActivity(goToTaskDetailIntent);
//    });
//  }
//
//  void setupTask2Button() {
//    Button Task2Button = findViewById(R.id.MainActivityTask2Button);
//    Task2Button.setOnClickListener(v -> {
//      String tasksName = ((Button)findViewById(R.id.MainActivityTask2Button)).getText().toString();
//      Intent goToTaskDetailIntent = new Intent(MainActivity.this, TaskDetailActivity.class);
//      goToTaskDetailIntent.putExtra(TASK_NAME_EXTRA_TAG, tasksName);
//      startActivity(goToTaskDetailIntent);
//    });
//  }
//
//  void setupTask3Button() {
//    Button Task3Button = findViewById(R.id.MainActivityTask3Button);
//    Task3Button.setOnClickListener(v -> {
//      String tasksName = ((Button)findViewById(R.id.MainActivityTask3Button)).getText().toString();
//      Intent goToTaskDetailIntent = new Intent(MainActivity.this, TaskDetailActivity.class);
//      goToTaskDetailIntent.putExtra(TASK_NAME_EXTRA_TAG, tasksName);
//      startActivity(goToTaskDetailIntent);
//    });
//  }


  void setupAddTasksButton() {
    Button addTasksButton = findViewById(R.id.MainActivityAddTasksButton);
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

        if(parent.getChildAdapterPosition(view) == tasks.size()-1) {
          outRect.bottom = 0;
        }
      }
    });

    TaskListRecyclerViewAdapter adapter = new TaskListRecyclerViewAdapter(tasks, this);


    TaskListsRecyclerView.setAdapter(adapter);
  }

  void createTaskInstances() {
    tasks.add(new Task("Wash Face", Type.COMPLETE));
    tasks.add(new Task("Brush Teeth", Type.COMPLETE));
    tasks.add(new Task("Wash Hands", Type.COMPLETE));
    tasks.add(new Task("Make Breakfast", Type.IN_PROGRESS));
    tasks.add(new Task("Clean Dishes", Type.ASSIGNED));
    tasks.add(new Task("Setup Computer", Type.ASSIGNED));
    tasks.add(new Task("Check Emails", Type.ASSIGNED));
    tasks.add(new Task("Open IDE", Type.ASSIGNED));
    tasks.add(new Task("Code Time", Type.ASSIGNED));
    tasks.add(new Task("Take Break", Type.NEW));
  }



}
