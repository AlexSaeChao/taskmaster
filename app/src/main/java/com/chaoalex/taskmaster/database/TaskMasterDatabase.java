package com.chaoalex.taskmaster.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.chaoalex.taskmaster.daos.TaskDao;
import com.chaoalex.taskmaster.models.Task;

@TypeConverters({TaskMasterDatabaseClassConverters.class})
@Database(entities = {Task.class}, version = 1)
public abstract class TaskMasterDatabase extends RoomDatabase {
  public abstract TaskDao taskDao();
}
