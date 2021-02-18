package com.apollo.task.service;

import com.apollo.task.model.Quiz;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface QuizService {

    Mono<Optional<Quiz>> getQuizById(final String quizId);

    Mono<Quiz> saveQuiz(final Mono<Quiz> quizMono);

    Mono<Boolean> updateQuiz(final Mono<Quiz> quizMono , final String quizOwnerId);

    Mono<Boolean> deleteQuiz(final String quizId , final String quizOwnerId);
}
