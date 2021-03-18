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

@Configuration
public class QuizRouterConfig {

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
