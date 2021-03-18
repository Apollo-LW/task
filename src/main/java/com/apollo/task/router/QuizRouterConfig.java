package com.apollo.task.router;

import com.apollo.task.constant.RoutingConstant;
import com.apollo.task.handler.QuizHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Configuration for the main quiz API router functions and connecting them with the {@link QuizHandler}
 */
@Configuration
public class QuizRouterConfig {

    /**
     * Main routing function for the Quiz API BASE URI will start with {@link RoutingConstant#QUIZ_PATH} and accept JSON data for all requests
     *
     * @param quizHandler the main quiz Handler to make operations on the quiz objects that are sent from the requests
     *
     * @return a {@link RouterFunction} with the handler function return type {@link ServerResponse}
     */
    @Bean
    public RouterFunction<ServerResponse> routeQuiz(final QuizHandler quizHandler) {
        return RouterFunctions
                .route()
                .path(RoutingConstant.QUIZ_PATH , routeFunctionBuilder ->
                        routeFunctionBuilder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON) , builder -> builder
                                .GET(RoutingConstant.QUIZ_ID_PATH , quizHandler::getQuizById)
                                .POST(quizHandler::createQuiz)
                                .PUT(RoutingConstant.OWNER_ID_PATH , quizHandler::updateQuiz)
                                .DELETE(RoutingConstant.OWNER_QUIZ_PATH , quizHandler::deleteQuiz)))
                .build();
    }

}
