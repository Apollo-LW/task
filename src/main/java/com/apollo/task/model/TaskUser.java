package com.apollo.task.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class TaskUser {

    private String userId;
    private Map<TaskType, Set<Task>> userTaskByType = new HashMap<>();
    private Map<TaskStatus, Set<Task>> userTaskByStatus = new HashMap<>();
    private Map<String, Set<Task>> userTaskByGroupName = new HashMap<>();

}

