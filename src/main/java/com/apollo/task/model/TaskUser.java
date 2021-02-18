package com.apollo.task.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class TaskUser {

    private String userId;
    private Map<TaskType, Set<Task>> userTaskByType = new HashMap<TaskType, Set<Task>>();
    private Map<TaskStatus, Set<Task>> userTaskByStatus = new HashMap<TaskStatus, Set<Task>>();
    private Map<String, Set<Task>> userTaskByGroupName = new HashMap<String, Set<Task>>();

}

