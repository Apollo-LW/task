package com.apollo.task.service;

import com.apollo.task.model.Task;
import com.apollo.task.model.TaskStatus;
import reactor.core.publisher.Flux;

import java.util.HashMap;

public interface TaskUserService {

    Flux<HashMap<TaskStatus,Task>> getUserTasks (String userId, String taskStatus);

    Flux<Task> getUserTasksByType (String userId, String taskType);

    Flux<Task> getUserTasksByStatus(String userId, String taskStatus);

    Flux<Task> getUserTasksByGroupName(String userId, String groupName);

}
