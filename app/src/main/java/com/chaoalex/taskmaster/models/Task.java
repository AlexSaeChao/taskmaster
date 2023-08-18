package com.chaoalex.taskmaster.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Task {
  @PrimaryKey(autoGenerate = true)
          public Long id;
  String title;
  String body;
  java.util.Date dateCreated;
  TaskCategoryEnum state;

  public Task(String title, String body, java.util.Date dateCreated, TaskCategoryEnum state) {
    this.title = title;
    this.body = body;
    this.dateCreated = dateCreated;
    this.state = state;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public TaskCategoryEnum getState() {
    return state;
  }

  public void setState(TaskCategoryEnum state) {
    this.state = state;
  }
}
