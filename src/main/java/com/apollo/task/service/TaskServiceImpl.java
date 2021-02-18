package com.apollo.task.service;

import com.apollo.task.kafka.KafkaService;
import com.apollo.task.model.Task;
import com.apollo.task.model.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

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

    @Override
    public Mono<Optional<Task>> getTaskById(String taskId) {
        return Mono.just(Optional.ofNullable(this.getTaskStateStore().get(taskId)));
    }

    @Override
    public Mono<Task> postTask(Mono<Task> taskMono) {
        return this.kafkaService.sendTaskRecord(taskMono).map(Optional::get);
    }

    @Override
    public Mono<Task> updateTask(Mono<Task> taskMono) {
        return taskMono.flatMap(task -> {
            Optional<Task> taskOptional = Optional.ofNullable(this.taskStateStore.get(task.getTaskId()));
            if(taskOptional.isEmpty()) return Mono.empty();
            Task updatedTask = taskOptional.get();
            updatedTask.setTaskName(task.getTaskName());
            updatedTask.setTaskOwners(task.getTaskOwners());
            updatedTask.setTaskMembers(task.getTaskMembers());
            updatedTask.setTaskType(task.getTaskType());
            updatedTask.setTaskStatus(task.getTaskStatus());
            updatedTask.setTaskGroupName(task.getTaskGroupName());
            updatedTask.setEventIDs(task.getEventIDs());
            return this.kafkaService.sendTaskRecord(taskMono).map(Optional::get);
        });
    }

    @Override
    public Mono<Boolean> deleteTask(String taskId) {
        Optional<Task> taskOptional = Optional.ofNullable(this.getTaskStateStore().get(taskId));
        if(taskOptional.isEmpty()) return Mono.empty();
        Task task = taskOptional.get();
        task.setTaskStatus(TaskStatus.REMOVED);
        return this.kafkaService.sendTaskRecord(Mono.just(task)).map(Optional::isPresent);
    }
}
