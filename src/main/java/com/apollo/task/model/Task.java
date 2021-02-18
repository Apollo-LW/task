package com.apollo.task.model;

import lombok.Data;

import java.util.*;

@Data
public class Task {

    private final String taskId = UUID.randomUUID().toString();
    private final Date taskCreationDate = Calendar.getInstance().getTime();
    private TaskType taskType;
    private Set<String> taskOwners = new HashSet<>();
    private Set<String> taskMembers = new HashSet<>();
    private String taskName = taskId + "-" + taskCreationDate;
    private String taskGroupName;
    private TaskStatus taskStatus = TaskStatus.TO_DO;
    private Set<String> eventIDs = new HashSet<>();

}

