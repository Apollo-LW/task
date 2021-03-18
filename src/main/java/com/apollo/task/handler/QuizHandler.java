package com.apollo.task.handler;

import com.apollo.task.constant.RoutingConstant;
import com.apollo.task.model.Quiz;
import com.apollo.task.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Main Handler for the Quiz API
 */
@Component
@RequiredArgsConstructor
public class QuizHandler {

    /**
     * Quiz Service to handle data and event operations
     */
    private final QuizService quizService;

    /**
     * Getting a certain quiz using it's ID
     *
     * @param request the request which has the quizId in it
     *
     * @return a {@link Quiz} object if it was found
     */
    public @NotNull Mono<ServerResponse> getQuizById(final ServerRequest request) {
        final String quizId = request.pathVariable(RoutingConstant.QUIZ_ID);
        final Mono<Quiz> quizMono = this.quizService.getQuizById(quizId).flatMap(Mono::justOrEmpty);
        return ServerResponse
                .ok().body(quizMono , Quiz.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

    /**
     * Creating a new Quiz
     *
     * @param request the request that has the request object in the request body
     *
     * @return the created quiz if it was created successfully
     */
    public @NotNull Mono<ServerResponse> createQuiz(final ServerRequest request) {
        final Mono<Quiz> quizMono = request.bodyToMono(Quiz.class);
        final Mono<Quiz> createdQuizMono = this.quizService.saveQuiz(quizMono).flatMap(Mono::justOrEmpty);
        return ServerResponse
                .ok().body(createdQuizMono , Quiz.class)
                .switchIfEmpty(ServerResponse.badRequest().build())
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

    /**
     * Updating an existing quiz
     *
     * @param request the request that have the updated quiz object in the body as well as the on of the owners ID
     *
     * @return if the quiz was updated successfully or not
     */
    public @NotNull Mono<ServerResponse> updateQuiz(final ServerRequest request) {
        final String quizOwnerId = request.pathVariable(RoutingConstant.OWNER_ID);
        final Mono<Quiz> quizMono = request.bodyToMono(Quiz.class);
        final Mono<Boolean> isUpdated = this.quizService.updateQuiz(quizMono , quizOwnerId);
        return ServerResponse
                .ok().body(isUpdated , Boolean.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

    /**
     * Deleting a quiz
     *
     * @param request the request that have the quiz ID that we are going to delete as well as one of the owners ID
     *
     * @return if the quiz was deleted successfully or not
     */
    public @NotNull Mono<ServerResponse> deleteQuiz(final ServerRequest request) {
        final String quizId = request.pathVariable(RoutingConstant.QUIZ_ID);
        final String quizOwnerId = request.pathVariable(RoutingConstant.OWNER_ID);
        final Mono<Boolean> isDeleted = this.quizService.deleteQuiz(quizId , quizOwnerId);
        return ServerResponse
                .ok().body(isDeleted , Boolean.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

}
