package com.apollo.task.service;

import com.apollo.task.model.Task;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface TaskService {

    Mono<Optional<Task>> getTaskById(final String taskId);

    Mono<Task> saveTask(final Mono<Task> taskMono);

    Mono<Task> updateTask(final Mono<Task> taskMono);

    Mono<Boolean> deleteTask(final String taskId);
}
