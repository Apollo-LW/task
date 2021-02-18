package com.apollo.task.controller;

import com.apollo.task.model.Task;
import com.apollo.task.model.TaskStatus;
import com.apollo.task.service.TaskUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.HashMap;

@RestController
@RequestMapping("/task/user")
@RequiredArgsConstructor
public class TaskUserController {

    private final TaskUserService taskUserService;

    @GetMapping("/{userId}/{taskType}")
    public Flux<Task> getUserTaskByType (@PathVariable("userId") String userId, @PathVariable ("taskType") String taskType){
        return this.taskUserService.getUserTasksByType(userId, taskType);
    }

    @GetMapping("/{userId}")
    public Flux<HashMap<TaskStatus, Task>> getUserTasks (@PathVariable ("userId") String userId){
        return this.taskUserService.getUserTasks(userId);
    }

    @GetMapping("/{userId}/{taskStatus}")
    public Flux<Task> getUserTaskByStatus (@PathVariable("userId") String userId, @PathVariable ("taskStatus") String taskStatus){
        return this.taskUserService.getUserTasksByStatus(userId,taskStatus);
    }

    @GetMapping("/{userId}/{groupName}")
    public Flux<Task> getUserTaskByGroupName (@PathVariable("userId") String userId, @PathVariable("groupName") String groupName){
        return this.taskUserService.getUserTasksByGroupName(userId,groupName);
    }
}
