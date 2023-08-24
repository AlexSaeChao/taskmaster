package com.chaoalex.taskmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chaoalex.taskmaster.MainActivity;
import com.chaoalex.taskmaster.R;
import com.chaoalex.taskmaster.activities.TaskDetailActivity;
import com.amplifyframework.datastore.generated.model.Task;


import java.util.List;

//public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter {

public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter<TaskListRecyclerViewAdapter.TaskListViewHolder> {

  List<Task> tasks;

  Context callingActivity;

  public TaskListRecyclerViewAdapter(List<Task> tasks, Context callingActivity) {
    this.tasks = tasks;
    this.callingActivity = callingActivity;
  }

  @NonNull
  @Override
  public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_list, parent, false);

    return new TaskListViewHolder(taskFragment);
  }

  @Override
  public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
    TextView taskFragmentTextView = (TextView) holder.itemView.findViewById(R.id.taskFragmentTextView);
//    DateFormat dateFormatter = new SimpleDateFormatter("MM/dd/yyyy HH:mm zzz");
    String taskFragmentText = (position + 1) + ". " + tasks.get(position).getTitle();
//            + "\n" + tasks.get(position).getBody()
//            + "\n" + tasks.get(position).getDateCreated()
//            + "\n" + tasks.get(position).getState();
    taskFragmentTextView.setText(taskFragmentText);

    View taskViewHolder = holder.itemView;
    taskViewHolder.setOnClickListener(view -> {
      Intent goToTaskDetailIntent = new Intent(callingActivity, TaskDetailActivity.class);
      goToTaskDetailIntent.putExtra(MainActivity.TASK_NAME_EXTRA_TAG, tasks.get(position).getTitle());
      goToTaskDetailIntent.putExtra(MainActivity.TASK_DESCRIPTION_EXTRA_TAG, tasks.get(position).getDescription());
      callingActivity.startActivity(goToTaskDetailIntent);
    });
  }

  @Override
  public int getItemCount() {
//    return 10;
    return tasks.size();
  }

  public static class TaskListViewHolder extends RecyclerView.ViewHolder {
    public TaskListViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }

}
