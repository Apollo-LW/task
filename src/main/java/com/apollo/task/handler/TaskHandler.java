package com.apollo.task.handler;

import com.apollo.task.constant.RoutingConstant;
import com.apollo.task.model.Task;
import com.apollo.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TaskHandler {

    private final TaskService taskService;

    public @NotNull Mono<ServerResponse> getTaskById(final ServerRequest request) {
        final String taskId = request.pathVariable(RoutingConstant.TASK_ID);
        final Mono<Task> taskMono = this.taskService.getTaskById(taskId).flatMap(Mono::justOrEmpty);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskMono , Task.class);
    }

    public @NotNull Mono<ServerResponse> createTask(final ServerRequest request) {
        final Mono<Task> taskMono = request.bodyToMono(Task.class);
        final Mono<Task> createdTask = this.taskService.saveTask(taskMono).flatMap(Mono::justOrEmpty);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdTask , Task.class);
    }

    public @NotNull Mono<ServerResponse> updateTask(final ServerRequest request) {
        final Mono<Task> taskMono = request.bodyToMono(Task.class);
        final String ownerId = request.pathVariable(RoutingConstant.OWNER_ID);
        final Mono<Boolean> isUpdated = this.taskService.updateTask(taskMono , ownerId);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(isUpdated , Boolean.class);
    }

    public @NotNull Mono<ServerResponse> deleteTask(final ServerRequest request) {
        final String taskId = request.pathVariable(RoutingConstant.TASK_ID);
        final String ownerId = request.pathVariable(RoutingConstant.OWNER_ID);
        final Mono<Boolean> isDeleted = this.taskService.deleteTask(taskId , ownerId);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(isDeleted , Boolean.class);
    }

}
