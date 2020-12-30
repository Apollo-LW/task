package com.apollo.task.service;

import com.apollo.task.model.Task;
import com.apollo.task.model.TaskStatus;
import com.apollo.task.model.TaskType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

    Flux<TaskType> getTaskTypes ();

    Flux<TaskStatus> getTaskStatus ();

    Mono<Task> getTaskById (String taskId);


}
