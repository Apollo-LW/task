package com.apollo.task.service;

import com.apollo.task.model.Task;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface TaskService {

    Mono<Optional<Task>> getTaskById (String taskId);

    Mono<Task> postTask (Mono<Task> taskMono);

    Mono<Task> updateTask (Mono<Task> taskMono);

   // Mono<Boolean> updateTask
    Mono<Boolean> deleteTask(String taskId);
}
