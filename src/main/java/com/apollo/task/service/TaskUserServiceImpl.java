package com.apollo.task.service;

import com.apollo.task.model.Task;
import com.apollo.task.model.TaskStatus;
import com.apollo.task.model.TaskUser;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.web.util.WebAppRootListener;
import reactor.core.publisher.Flux;


import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class TaskUserServiceImpl implements TaskUserService {

    @Value("${task.user.kafka.store}")
    private String taskUserStateStoreName;
    private ReadOnlyKeyValueStore<String, TaskUser> taskUserStateStore;
    private final InteractiveQueryService interactiveQueryService;

    private ReadOnlyKeyValueStore<String, TaskUser> getTaskUserStateStore(){
        if(this.taskUserStateStore == null)
            this.taskUserStateStore = this.interactiveQueryService.getQueryableStore(this.taskUserStateStoreName , QueryableStoreTypes.keyValueStore());
        return this.taskUserStateStore;
    }


    @Override
    public Flux<HashMap<TaskStatus, Task>> getUserTasks(String userId, String taskStatus) {
        return Flux.fromIterable(getTaskUserStateStore().get(userId).getUserTaskByStatus().values());
        // I don't know if this is correct
    }

    @Override
    public Flux<Task> getUserTasksByType(String userId, String taskType) {
        if(taskType == null){
        return Flux.fromIterable(getTaskUserStateStore().get(userId).getUserTaskByType().values());
        }
        else{
            List<Task> userTasks = (List<Task>) getTaskUserStateStore().get(userId).getUserTaskByType().get(taskType);
            return Flux.fromIterable(userTasks);
        }
    }

    @Override
    public Flux<Task> getUserTasksByStatus(String userId, String taskStatus) {
        if(taskStatus == null){
            return Flux.fromIterable(getTaskUserStateStore().get(userId).getUserTaskByStatus().values());
        }
        else{
            List<Task> userTasks = (List<Task>) getTaskUserStateStore().get(userId).getUserTaskByStatus().get(taskStatus);
            return Flux.fromIterable(userTasks);
        }
    }

    @Override
    public Flux<Task> getUserTasksByGroupName(String userId, String groupName) {
        if(groupName == null){
            return Flux.fromIterable(getTaskUserStateStore().get(userId).getUserTaskByGroupName().values());
        }
        else{
            List<Task> userTasks = (List<Task>) getTaskUserStateStore().get(userId).getUserTaskByGroupName().get(groupName);
            return Flux.fromIterable(userTasks);
        }
    }


}
