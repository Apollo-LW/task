package com.apollo.task.model;

import lombok.Data;

import java.util.*;

@Data
public class TaskUser {

    private String userId;
    private Map<TaskType, Set<Task>> userTaskByType = new HashMap<>();
    private Map<TaskStatus, Set<Task>> userTaskByStatus = new HashMap<>();
    private Map<String, Set<Task>> userTaskByGroupName = new HashMap<>();

    public void addUserTaskByType(final TaskType taskType , final Task task) {
        final Set<Task> tasks = ( this.userTaskByType.containsKey(taskType) ) ? this.userTaskByType.get(taskType) : new HashSet<>();
        tasks.add(task);
        this.userTaskByType.put(taskType , tasks);
    }

    public void addUserTaskByStatus(final TaskStatus taskStatus , final Task task) {
        final Set<Task> tasks = ( this.userTaskByStatus.containsKey(taskStatus) ) ? this.userTaskByStatus.get(taskStatus) : new HashSet<>();
        tasks.add(task);
        this.userTaskByStatus.put(taskStatus , tasks);
    }


    public void addUserTaskByGroupName(final String taskGroupName , final Task task) {
        final String upperCaseGroupName = taskGroupName.toUpperCase(Locale.ROOT);
        final Set<Task> tasks = ( this.userTaskByGroupName.containsKey(upperCaseGroupName) ) ? this.userTaskByGroupName.get(upperCaseGroupName) : new HashSet<>();
        tasks.add(task);
        this.userTaskByGroupName.put(upperCaseGroupName , tasks);
    }
}
