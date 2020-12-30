package com.apollo.task.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;

@Data
@RequiredArgsConstructor
public class TaskUser extends Task{

    private String userId;
    private HashMap userTaskByType = new HashMap<TaskType , HashSet<Task>>();
    private HashMap userTaskByStatus = new HashMap <TaskStatus , HashSet<Task>>();
    private HashMap userTaskByGroupName = new HashMap<String , HashSet<Task>>();

}
