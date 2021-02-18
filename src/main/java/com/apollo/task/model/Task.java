package com.apollo.task.model;

import lombok.Data;

import java.util.*;

@Data
public class Task {

    private final String taskId = UUID.randomUUID().toString();
    private final Date taskCreationDate = Calendar.getInstance().getTime();
    private TaskType taskType;
    private String taskGroupName, taskName = taskId + "-" + taskCreationDate;
    private TaskStatus taskStatus = TaskStatus.TO_DO;
    private Set<String> taskOwners = new HashSet<>(), taskMembers = new HashSet<>(), eventIDs = new HashSet<>();

    public boolean doesNotHaveOwner(String ownerId) {
        return !this.taskOwners.contains(ownerId);
    }
}

