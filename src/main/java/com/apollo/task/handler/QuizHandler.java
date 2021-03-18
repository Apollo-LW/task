package com.apollo.task.handler;

import com.apollo.task.constant.RoutingConstant;
import com.apollo.task.model.Quiz;
import com.apollo.task.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@Component
@RequiredArgsConstructor
public class QuizHandler {

    private final QuizService quizService;

    public @NotNull Mono<ServerResponse> getQuizById(final ServerRequest request) {
        final String quizId = request.pathVariable(RoutingConstant.QUIZ_ID);
        final Mono<Quiz> quizMono = this.quizService.getQuizById(quizId).flatMap(Mono::justOrEmpty);
        return ServerResponse
                .ok().body(quizMono , Quiz.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

    public @NotNull Mono<ServerResponse> createQuiz(final ServerRequest request) {
        final Mono<Quiz> quizMono = request.bodyToMono(Quiz.class);
        final Mono<Quiz> createdQuizMono = this.quizService.saveQuiz(quizMono).flatMap(Mono::justOrEmpty);
        return ServerResponse
                .ok().body(createdQuizMono , Quiz.class)
                .switchIfEmpty(ServerResponse.badRequest().build())
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

    public @NotNull Mono<ServerResponse> updateQuiz(final ServerRequest request) {
        final String quizOwnerId = request.pathVariable(RoutingConstant.OWNER_ID);
        final Mono<Quiz> quizMono = request.bodyToMono(Quiz.class);
        final Mono<Boolean> isUpdated = this.quizService.updateQuiz(quizMono , quizOwnerId);
        return ServerResponse
                .ok().body(isUpdated , Boolean.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(throwable -> ServerResponse.badRequest().build());
    }

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
