package com.apollo.task.service.impl;

import com.apollo.task.kafka.KafkaService;
import com.apollo.task.model.Task;
import com.apollo.task.model.TaskStatus;
import com.apollo.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    @Value("${task.kafka.store}")
    private String taskStateStoreName;
    private final InteractiveQueryService interactiveQueryService;
    private final KafkaService kafkaService;
    private ReadOnlyKeyValueStore<String, Task> taskStateStore;

    private ReadOnlyKeyValueStore<String, Task> getTaskStateStore() {
        if (this.taskStateStore == null)
            this.taskStateStore = interactiveQueryService.getQueryableStore(this.taskStateStoreName , QueryableStoreTypes.keyValueStore());
        return this.taskStateStore;
    }

    private boolean isNotValid(final Optional<Task> taskOptional , final String taskOwnerId) {
        return taskOptional.isEmpty() || taskOptional.get().doesNotHaveOwner(taskOwnerId);
    }

    @Override
    public Mono<Optional<Task>> getTaskById(final String taskId) {
        return Mono.just(Optional.ofNullable(this.getTaskStateStore().get(taskId)));
    }

    @Override
    public Mono<Optional<Task>> saveTask(final Mono<Task> taskMono) {
        return this.kafkaService.sendTaskRecord(taskMono);
    }

    @Override
    public Mono<Boolean> updateTask(final Mono<Task> taskMono , final String taskOwnerId) {
        return taskMono.flatMap(task -> this.getTaskById(task.getTaskId()).flatMap(taskOptional -> {
            if (this.isNotValid(taskOptional , taskOwnerId)) return Mono.just(false);
            Task updatedTask = taskOptional.get();
            updatedTask.setTaskName(task.getTaskName());
            updatedTask.setTaskOwners(task.getTaskOwners());
            updatedTask.setTaskMembers(task.getTaskMembers());
            updatedTask.setTaskType(task.getTaskType());
            updatedTask.setTaskStatus(task.getTaskStatus());
            updatedTask.setTaskGroupName(task.getTaskGroupName());
            updatedTask.setEventIDs(task.getEventIDs());
            return this.kafkaService.sendTaskRecord(Mono.just(updatedTask)).map(Optional::isPresent);
        }));
    }

    @Override
    public Mono<Boolean> deleteTask(final String taskId , final String taskOwnerId) {
        return this.getTaskById(taskId).flatMap(taskOptional -> {
            if (this.isNotValid(taskOptional , taskOwnerId)) return Mono.just(false);
            Task task = taskOptional.get();
            task.setTaskStatus(TaskStatus.REMOVED);
            return this.kafkaService.sendTaskRecord(Mono.just(task)).map(Optional::isPresent);
        });
    }
}
