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

/**
 * Main Task handler for the Task API
 */
@Component
@RequiredArgsConstructor
public class TaskHandler {

    /**
     * Task Service to handle data and event operations
     */
    private final TaskService taskService;

    /**
     * Getting a certain task in it's final state using the {@link Task#getTaskId()}
     *
     * @param request the request that led for this method to execute, which contains the taskId in the path variable {@link RoutingConstant#TASK_ID}
     *
     * @return a {@link ServerResponse} with the task object in it, parsed in JSON. If the task was not found the response will indicate that
     */
    public @NotNull Mono<ServerResponse> getTaskById(final ServerRequest request) {
        final String taskId = request.pathVariable(RoutingConstant.TASK_ID);
        final Mono<Task> taskMono = this.taskService.getTaskById(taskId).flatMap(Mono::justOrEmpty);
        return taskMono
                .flatMap(task -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(task , Task.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Create a new task event
     *
     * @param request the request that led for this method to execute, which contain the {@link Task} object in the body
     *
     * @return a {@link ServerResponse} with the created {@link Task} in the response. If the creation had an error in it the response will indicate that
     */
    public @NotNull Mono<ServerResponse> createTask(final ServerRequest request) {
        final Mono<Task> taskMono = request.bodyToMono(Task.class);
        final Mono<Task> createdTask = this.taskService.saveTask(taskMono).flatMap(Mono::justOrEmpty);
        return createdTask
                .flatMap(task -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(task , Task.class))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }

    /**
     * Updating the task event
     *
     * @param request the request that led for this method to execute, which contain the {@link Task} object in the body,
     *                as well as one of the {@link Task#getTaskOwners()} in the path variable {@link RoutingConstant#OWNER_ID}
     *
     * @return a {@link ServerResponse} with the a boolean flag indicating if the update was successful or not.
     */
    public @NotNull Mono<ServerResponse> updateTask(final ServerRequest request) {
        final Mono<Task> taskMono = request.bodyToMono(Task.class);
        final String ownerId = request.pathVariable(RoutingConstant.OWNER_ID);
        final Mono<Boolean> isUpdated = this.taskService.updateTask(taskMono , ownerId);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(isUpdated , Boolean.class);
    }

    /**
     * Delete the task event
     *
     * @param request the request that led for this method to execute, which contain the {@link Task#getTaskId()} in the path variable {@link RoutingConstant#TASK_ID},
     *                as well as one of the {@link Task#getTaskOwners()} in the path variable {@link RoutingConstant#OWNER_ID}
     *
     * @return a {@link ServerResponse} with a boolean flag indicating if the delete was successful or not
     */
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
