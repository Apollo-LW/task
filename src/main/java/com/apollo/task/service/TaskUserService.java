package com.apollo.task.service;

import com.apollo.task.model.Task;
import reactor.core.publisher.Flux;

/**
 * Main Task and User Join Service,
 * this is just an abstraction of what can be done
 */
public interface TaskUserService {

    Flux<Task> getUserTasks(final String userId);

    Flux<Task> getUserTasksByType(final String userId , final String taskType);

    Flux<Task> getUserTasksByStatus(final String userId , final String taskStatus);

    Flux<Task> getUserTasksByGroupName(final String userId , String groupName);
}
