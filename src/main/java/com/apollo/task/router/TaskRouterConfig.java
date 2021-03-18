package com.apollo.task.router;

import com.apollo.task.constant.RoutingConstant;
import com.apollo.task.handler.TaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Configuration for the main Task API router functions, to be able to route task requests to the correct handler
 */
@Configuration
public class TaskRouterConfig {

    /**
     * Main routing function for the Task API
     * Base URI will start with {@link RoutingConstant#TASK_PATH}
     * and accept JSON data for all requests
     *
     * @param taskHandler the main handler for task operations
     *
     * @return a {@link RouterFunction} with the handler functions return type, which is a {@link ServerResponse} in this case
     */
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
