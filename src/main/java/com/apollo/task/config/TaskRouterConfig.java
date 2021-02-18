package com.apollo.task.config;

import com.apollo.task.constant.RoutingConstant;
import com.apollo.task.handler.TaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TaskRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routeTask(final TaskHandler taskHandler) {
        return RouterFunctions
                .route()
                .path(RoutingConstant.TASK_PATH , routeFunctionBuilder ->
                        routeFunctionBuilder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON) , builder -> builder
                                .GET(RoutingConstant.TASK_ID_PATH , taskHandler::getTaskById)
                                .POST(taskHandler::createTask)
                                .PUT(taskHandler::updateTask)
                                .DELETE(taskHandler::deleteTask)))
                .build();
    }

}
