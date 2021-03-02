package com.apollo.task.handler;

import com.apollo.task.constant.RoutingConstant;
import com.apollo.task.model.Task;
import com.apollo.task.service.TaskUserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TaskUserHandler {

    private final TaskUserService taskUserService;

    public @NotNull Mono<ServerResponse> getUserTasks(final ServerRequest request) {
        final String userId = request.pathVariable(RoutingConstant.USER_ID);
        final Flux<Task> userTasksFlux = this.taskUserService.getUserTasks(userId);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userTasksFlux , Task.class)
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

    public @NotNull Mono<ServerResponse> getUserTaskByType(final ServerRequest request) {
        final String userId = request.pathVariable(RoutingConstant.USER_ID);
        final String taskType = request.pathVariable(RoutingConstant.TASK_TYPE);
        final Flux<Task> userTasksByTypeFlux = this.taskUserService.getUserTasksByType(userId , taskType);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userTasksByTypeFlux , Task.class)
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

    public @NotNull Mono<ServerResponse> getUserTaskByStatus(final ServerRequest request) {
        final String userId = request.pathVariable(RoutingConstant.USER_ID);
        final String taskStatus = request.pathVariable(RoutingConstant.TASK_STATUS);
        final Flux<Task> userTasksByStatusFlux = this.taskUserService.getUserTasksByStatus(userId , taskStatus);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userTasksByStatusFlux , Task.class)
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

    public @NotNull Mono<ServerResponse> getUserTaskByGroupName(final ServerRequest request) {
        final String userId = request.pathVariable(RoutingConstant.USER_ID);
        final String groupName = request.pathVariable(RoutingConstant.GROUP_NAME);
        final Flux<Task> userTasksByGroupNameFlux = this.taskUserService.getUserTasksByGroupName(userId , groupName);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userTasksByGroupNameFlux , Task.class)
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

}
