package com.apollo.task.service.impl;

import com.apollo.task.constant.ErrorConstant;
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

import java.util.Locale;
import java.util.Optional;

/**
 * Main {@link TaskUserService} Implementation to deal with events and Kafka
 */
@Service
@RequiredArgsConstructor
public class TaskUserServiceImpl implements TaskUserService {

    /**
     * {@link InteractiveQueryService} to get the Task User State Store
     */
    private final InteractiveQueryService interactiveQueryService;
    /**
     * Task and User topic name
     */
    @Value("${task.user.kafka.store}")
    private String taskUserStateStoreName;
    /**
     * Main Task User State Store
     */
    private ReadOnlyKeyValueStore<String, TaskUser> taskUserStateStore;

    /**
     * Get the {@link ReadOnlyKeyValueStore} from the {@link InteractiveQueryService} so it can be accessed
     *
     * @return a {@link ReadOnlyKeyValueStore} which is the Task User State Store
     */
    private ReadOnlyKeyValueStore<String, TaskUser> getTaskUserStateStore() {
        if (this.taskUserStateStore == null)
            this.taskUserStateStore = this.interactiveQueryService.getQueryableStore(this.taskUserStateStoreName , QueryableStoreTypes.keyValueStore());
        return this.taskUserStateStore;
    }

    /**
     * Return a {@link TaskUser} object in it's final state
     *
     * @param userId the userId of the {@link TaskUser#getUserId()} that we want
     *
     * @return a {@link Optional} of the TaskUser
     */
    private Mono<Optional<TaskUser>> getUserById(final String userId) {
        if (userId == null)
            return Mono.error(new NullPointerException(ErrorConstant.USER_ID_NULL));
        if (userId.length() == 0)
            return Mono.error(new IllegalArgumentException(ErrorConstant.USER_ID_EMPTY));

        return Mono.just(Optional.ofNullable(this.getTaskUserStateStore().get(userId)));
    }

    /**
     * get the enum value of a string
     *
     * @param s the string to get the enum from
     *
     * @return a the string in ALL CAPS and underscores instead of spaces
     */
    private String toEnumString(final String s) {
        if (s == null)
            return null;
        String enumString = s.toUpperCase(Locale.ROOT);
        enumString = enumString.replaceAll(" " , "_");
        return enumString;
    }

    /**
     * Get user tasks
     *
     * @param userId the userId of the {@link TaskUser} that we want the tasks to
     *
     * @return all user tasks
     */
    @Override
    public Flux<Task> getUserTasks(final String userId) {
        if (userId == null)
            return Flux.error(new NullPointerException(ErrorConstant.USER_ID_NULL));
        if (userId.length() == 0)
            return Flux.error(new IllegalArgumentException(ErrorConstant.USER_ID_EMPTY));

        return this.getUserById(userId).flatMapMany(taskUserOptional -> {
            if (taskUserOptional.isEmpty()) return Flux.empty();
            return Flux.fromIterable(taskUserOptional.get().getUserTaskByType().values()).flatMap(Flux::fromIterable);
        });
    }

    /**
     * Get user tasks by {@link TaskType}
     *
     * @param userId   the userId of the {@link TaskUser} that we want the tasks to
     * @param taskType the {@link TaskType} of the tasks that we want
     *
     * @return all user tasks that is associated with the {@link TaskType}
     */
    @Override
    public Flux<Task> getUserTasksByType(final String userId , final String taskType) {
        if (userId == null)
            return Flux.error(new NullPointerException(ErrorConstant.USER_ID_NULL));
        if (userId.length() == 0)
            return Flux.error(new IllegalArgumentException(ErrorConstant.USER_ID_EMPTY));

        if (taskType == null) return this.getUserTasks(userId);

        return this.getUserById(userId).flatMapMany(taskUserOptional -> {
            if (taskUserOptional.isEmpty()) return Flux.empty();
            final String enumTaskType = this.toEnumString(taskType);
            return Flux.fromIterable(taskUserOptional.get().getUserTaskByType().get(TaskType.valueOf(taskType)));
        });
    }

    /**
     * Get user tasks by {@link TaskStatus}
     *
     * @param userId     the userId of the {@link TaskUser} that we want the tasks to
     * @param taskStatus the {@link TaskStatus} of the tasks that we want
     *
     * @return all user tasks that is associated with {@link TaskStatus}
     */
    @Override
    public Flux<Task> getUserTasksByStatus(final String userId , final String taskStatus) {
        if (userId == null)
            return Flux.error(new NullPointerException(ErrorConstant.USER_ID_NULL));
        if (userId.length() == 0)
            return Flux.error(new IllegalArgumentException(ErrorConstant.USER_ID_EMPTY));

        if (taskStatus == null) return this.getUserTasks(userId);

        return this.getUserById(userId).flatMapMany(taskUserOptional -> {
            if (taskUserOptional.isEmpty()) return Flux.empty();
            final String enumTaskStatus = this.toEnumString(taskStatus);
            return Flux.fromIterable(taskUserOptional.get().getUserTaskByStatus().get(TaskStatus.valueOf(taskStatus)));
        });
    }

    /**
     * Get user tasks by {@link Task#getTaskGroupName()}
     *
     * @param userId    the userId of the {@link TaskUser} that we want the tasks to
     * @param groupName the group name of the task
     *
     * @return all user tasks that is associated with the group name
     */
    @Override
    public Flux<Task> getUserTasksByGroupName(final String userId , final String groupName) {
        if (userId == null)
            return Flux.error(new NullPointerException(ErrorConstant.USER_ID_NULL));
        if (userId.length() == 0)
            return Flux.error(new IllegalArgumentException(ErrorConstant.USER_ID_EMPTY));

        if (groupName == null) return this.getUserTasks(userId);

        return this.getUserById(userId).flatMapMany(taskUserOptional -> {
            if (taskUserOptional.isEmpty()) return Flux.empty();
            final String upperCaseGroupName = groupName.toUpperCase(Locale.ROOT);
            return Flux.fromIterable(taskUserOptional.get().getUserTaskByGroupName().get(groupName));
        });
    }
}
