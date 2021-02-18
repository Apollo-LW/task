package com.apollo.task.service;

import com.apollo.task.model.Task;
import com.apollo.task.model.TaskStatus;
import reactor.core.publisher.Flux;

import java.util.HashMap;

public interface TaskUserService {

    Flux<HashMap<TaskStatus, Task>> getUserTasks(final String userId);

    Flux<Task> getUserTasksByType(final String userId , final String taskType);

    Flux<Task> getUserTasksByStatus(final String userId , final String taskStatus);

    Flux<Task> getUserTasksByGroupName(final String userId , final String groupName);

}
