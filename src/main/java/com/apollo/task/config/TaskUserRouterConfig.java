package com.apollo.task.config;

import com.apollo.task.constant.RoutingConstant;
import com.apollo.task.handler.TaskUserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TaskUserRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routeTaskUser(final TaskUserHandler taskUserHandler) {
        return RouterFunctions
                .route()
                .path(RoutingConstant.TASK_USER_PATH , routeFunctionBuilder ->
                        routeFunctionBuilder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON) , builder -> builder
                                .GET(taskUserHandler::getUserTasks)
                                .GET(RoutingConstant.TASK_TYPE_PATH , taskUserHandler::getUserTaskByType)
                                .GET(RoutingConstant.TASK_STATUS_PATH , taskUserHandler::getUserTaskByStatus)
                                .GET(RoutingConstant.GROUP_NAME_PATH , taskUserHandler::getUserTaskByGroupName)))
                .build();
    }

}
