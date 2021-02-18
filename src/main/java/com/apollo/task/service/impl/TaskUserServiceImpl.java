package com.apollo.task.service.impl;

import com.apollo.task.model.Task;
import com.apollo.task.model.TaskStatus;
import com.apollo.task.model.TaskType;
import com.apollo.task.model.TaskUser;
import com.apollo.task.service.TaskUserService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskUserServiceImpl implements TaskUserService {

    @Value("${task.user.kafka.store}")
    private String taskUserStateStoreName;
    private final InteractiveQueryService interactiveQueryService;
    private ReadOnlyKeyValueStore<String, TaskUser> taskUserStateStore;

    private ReadOnlyKeyValueStore<String, TaskUser> getTaskUserStateStore() {
        if (this.taskUserStateStore == null)
            this.taskUserStateStore = this.interactiveQueryService.getQueryableStore(this.taskUserStateStoreName , QueryableStoreTypes.keyValueStore());
        return this.taskUserStateStore;
    }

    private Mono<Optional<TaskUser>> getUserById(final String userId) {
        return Mono.just(Optional.ofNullable(this.getTaskUserStateStore().get(userId)));
    }

    @Override
    public Flux<Task> getUserTasks(final String userId) {
        return this.getUserById(userId).flatMapMany(taskUserOptional -> {
            if (taskUserOptional.isEmpty()) return Flux.empty();
            return Flux.fromIterable(taskUserOptional.get().getUserTaskByType().values()).flatMap(Flux::fromIterable);
        });
    }

    @Override
    public Flux<Task> getUserTasksByType(final String userId , final String taskType) {
        if (taskType == null) return this.getUserTasks(userId);
        return this.getUserById(userId).flatMapMany(taskUserOptional -> {
            if (taskUserOptional.isEmpty()) return Flux.empty();
            return Flux.fromIterable(taskUserOptional.get().getUserTaskByType().get(TaskType.valueOf(taskType)));
        });
    }

    @Override
    public Flux<Task> getUserTasksByStatus(final String userId , final String taskStatus) {
        if (taskStatus == null) return this.getUserTasks(userId);
        return this.getUserById(userId).flatMapMany(taskUserOptional -> {
            if (taskUserOptional.isEmpty()) return Flux.empty();
            return Flux.fromIterable(taskUserOptional.get().getUserTaskByStatus().get(TaskStatus.valueOf(taskStatus)));
        });
    }

    @Override
    public Flux<Task> getUserTasksByGroupName(final String userId , final String groupName) {
        if (groupName == null) return this.getUserTasks(userId);
        return this.getUserById(userId).flatMapMany(taskUserOptional -> {
            if (taskUserOptional.isEmpty()) return Flux.empty();
            return Flux.fromIterable(taskUserOptional.get().getUserTaskByGroupName().get(groupName));
        });
    }
}
