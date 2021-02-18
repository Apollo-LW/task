package com.apollo.task.service;

import com.apollo.task.model.Quiz;
import com.apollo.task.model.Task;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface QuizService {

    Mono<Optional<Quiz>> getQuizById (String quizId);

    Mono<Quiz> postQuiz (Mono<Quiz> quizMono);

    Mono<Quiz> updateQuiz (Mono<Quiz> quizMono);

    Mono<Boolean> deleteQuiz(String quizId);
}
