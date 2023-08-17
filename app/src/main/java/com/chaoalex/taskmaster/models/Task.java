package com.chaoalex.taskmaster.models;

public class Task {
  String name;
  Type taskType;

  public Task(String name, Type taskType) {
    this.name = name;
    this.taskType = taskType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
