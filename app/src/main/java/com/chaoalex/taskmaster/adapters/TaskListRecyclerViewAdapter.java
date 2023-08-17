package com.chaoalex.taskmaster.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chaoalex.taskmaster.R;
import com.chaoalex.taskmaster.models.Task;

import java.util.List;

//public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter {

public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter<TaskListRecyclerViewAdapter.TaskListViewHolder> {


  List<Task> tasks;

  public TaskListRecyclerViewAdapter(List<Task> tasks) {
    this.tasks = tasks;
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
    String taskFragmentText = (position + 1) + ". " + tasks.get(position).getName();
    taskFragmentTextView.setText(taskFragmentText);
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
