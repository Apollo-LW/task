package com.apollo.task.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Task {

    private final String taskId = UUID.randomUUID().toString();
    private final Date taskCreationDate = Calendar.getInstance().getTime();
    private TaskType taskType;
    private HashSet<String> taskOwners = new HashSet<>();
    private HashSet<String> taskMembers = new HashSet<>();
    private String taskName = taskId + "-" + taskCreationDate;
    private String taskGroupName;
    private TaskStatus taskStatus = TaskStatus.TO_DO;
    private HashSet<String> eventIDs = new HashSet<>();

}

