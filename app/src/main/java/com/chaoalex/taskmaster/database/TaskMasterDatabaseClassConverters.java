package com.chaoalex.taskmaster.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class TaskMasterDatabaseClassConverters {
  @TypeConverter
  public static Date fromTimeStamp(Long value) {
    return value == null ? null : new Date(value);
  }

  @TypeConverter
  public static Long dateToTimestamp(Date date) {
    return date == null ? null : date.getTime();
  }
}