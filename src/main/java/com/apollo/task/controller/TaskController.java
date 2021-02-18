package com.apollo.task.controller;

import com.apollo.task.model.Task;
import com.apollo.task.service.TaskService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class TaskController {

    private TaskService taskService;


    @GetMapping(value ="/{taskId}")
    public Mono<Task> getTaskById (@PathVariable String taskId){
        return this.taskService.getTaskById(taskId).map(Optional::get);
    }

    @PostMapping(value = "/")
    public Mono<Task> createTask(@RequestBody Task task){
        return this.taskService.postTask(Mono.just(task));
    }

    @PutMapping(value= "/")
    public Mono<Task> updateTask(@RequestBody Task task){
        return this.taskService.updateTask(Mono.just(task));
    }


    @DeleteMapping("/")
    public Mono<Boolean> deleteTask(@RequestBody String taskId) {
        return this.taskService.deleteTask(taskId);
    }

}
