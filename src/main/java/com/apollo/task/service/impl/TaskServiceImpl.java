package com.apollo.task.service.impl;

import com.apollo.task.kafka.service.KafkaService;
import com.apollo.task.model.Quiz;
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

/**
 * Main {@link TaskService} implementation to deal with events and kafka
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    /**
     * {@link InteractiveQueryService} to get the Task State Store
     */
    private final InteractiveQueryService interactiveQueryService;
    /**
     * {@link KafkaService} to produce events
     */
    private final KafkaService kafkaService;
    /**
     * Task Topic Name
     */
    @Value("${task.kafka.store}")
    private String taskStateStoreName;
    /**
     * Main Task State Store
     */
    private ReadOnlyKeyValueStore<String, Task> taskStateStore;

    /**
     * Get the {@link ReadOnlyKeyValueStore} from the {@link InteractiveQueryService} so it can be accessed
     *
     * @return a {@link ReadOnlyKeyValueStore} which is the Task State Store
     */
    private ReadOnlyKeyValueStore<String, Task> getTaskStateStore() {
        if (this.taskStateStore == null)
            this.taskStateStore = interactiveQueryService.getQueryableStore(this.taskStateStoreName , QueryableStoreTypes.keyValueStore());
        return this.taskStateStore;
    }

    /**
     * Checking if a task is valid to be returned or processed
     *
     * @param taskOptional the task object to check, it's is an {@link Optional} so we can handle null checks
     * @param taskOwnerId  one of the task object owners {@link Quiz#getQuizOwnerId()} to check
     *
     * @return true if and only if the task object is not null, and the owner is valid
     */
    private boolean isNotValid(final Optional<Task> taskOptional , final String taskOwnerId) {
        return taskOptional.isEmpty() || taskOptional.get().doesNotHaveOwner(taskOwnerId);
    }

    /**
     * Get a task in it's final state
     *
     * @param taskId the tsakId of the task that we want
     *
     * @return an {@link Optional} of the task
     */
    @Override
    public Mono<Optional<Task>> getTaskById(final String taskId) {
        return Mono.just(Optional.ofNullable(this.getTaskStateStore().get(taskId)));
    }

    /**
     * Produce a Task
     *
     * @param taskMono the task event to be produced
     *
     * @return an {@link Optional} of the event produced
     */
    @Override
    public Mono<Optional<Task>> saveTask(final Mono<Task> taskMono) {
        return this.kafkaService.sendTaskRecord(taskMono);
    }

    /**
     * Produce an updated Task
     *
     * @param taskMono    the updated task to produce
     * @param taskOwnerId one of the {@link Task#getTaskOwners()} before of the update
     *
     * @return true if and only if the update was done successfully
     */
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

    /**
     * Disable a task
     *
     * @param taskId      the task to be deleted
     * @param taskOwnerId one of the {@link Task#getTaskOwners()}
     *
     * @return true if and only if the delete was done successfully
     */
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
