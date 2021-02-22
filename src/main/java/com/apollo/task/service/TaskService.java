package com.apollo.task.service;

import com.apollo.task.model.Task;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Main Task Operations,
 * this is an abstraction of what can be done
 */
public interface TaskService {

    Mono<Optional<Task>> getTaskById(final String taskId);

    Mono<Optional<Task>> saveTask(final Mono<Task> taskMono);

    Mono<Boolean> updateTask(final Mono<Task> taskMono , final String taskOwnerId);

    Mono<Boolean> deleteTask(final String taskId , final String taskOwnerId);
}
