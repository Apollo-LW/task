package com.apollo.task.model;

import lombok.Data;

import java.util.*;

@Data
public class Task {

    private final String taskId = UUID.randomUUID().toString();
    private final Date taskCreationDate = Calendar.getInstance().getTime();
    private TaskType taskType;
    private String taskGroupName, taskName = taskId + "-" + taskCreationDate;
    private TaskStatus taskStatus = TaskStatus.TODO;
    private Set<String> taskOwners = new HashSet<>(), taskMembers = new HashSet<>(), eventIDs = new HashSet<>();

    public boolean doesNotHaveOwner(String ownerId) {
        return !this.taskOwners.contains(ownerId);
    }

    public Set<String> getAllTaskMembers() {
        Set<String> allTaskMembers = new HashSet<>();
        allTaskMembers.addAll(this.taskMembers);
        allTaskMembers.addAll(this.taskOwners);
        return allTaskMembers;
    }
}

