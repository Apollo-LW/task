package com.apollo.task.service.impl;

import com.apollo.task.kafka.KafkaService;
import com.apollo.task.model.Quiz;
import com.apollo.task.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    @Value("${quiz.kafka.store}")
    private String quizStateStoreName;
    private final InteractiveQueryService interactiveQueryService;
    private final KafkaService kafkaService;
    private ReadOnlyKeyValueStore<String, Quiz> quizStateStore;

    private ReadOnlyKeyValueStore<String, Quiz> getQuizStateStore() {
        if (this.quizStateStore == null)
            this.quizStateStore = interactiveQueryService.getQueryableStore(this.quizStateStoreName , QueryableStoreTypes.keyValueStore());
        return this.quizStateStore;
    }

    private boolean isNotValid(final Optional<Quiz> quizOptional , final String quizOwnerId) {
        return quizOptional.isEmpty() || !quizOptional.get().getQuizOwnerId().equals(quizOwnerId);
    }

    @Override
    public Mono<Optional<Quiz>> getQuizById(final String quizId) {
        return Mono.just(Optional.ofNullable(this.getQuizStateStore().get(quizId)));
    }

    @Override
    public Mono<Optional<Quiz>> saveQuiz(final Mono<Quiz> quizMono) {
        return this.kafkaService.sendQuizRecord(quizMono);
    }

    @Override
    public Mono<Boolean> updateQuiz(final Mono<Quiz> quizMono , final String quizOwnerId) {
        return quizMono.flatMap(quiz -> this.getQuizById(quiz.getQuizId()).flatMap(quizOptional -> {
            if (this.isNotValid(quizOptional , quizOwnerId)) return Mono.just(false);
            final Quiz updatedQuiz = quizOptional.get();
            updatedQuiz.setQuizAnswer(quiz.getQuizAnswer());
            updatedQuiz.setQuizChapterId(quiz.getQuizChapterId());
            updatedQuiz.setQuizName(quiz.getQuizName());
            updatedQuiz.setQuizCourseId(quiz.getQuizCourseId());
            return this.kafkaService.sendQuizRecord(Mono.just(updatedQuiz)).map(Optional::isPresent);
        }));
    }

    @Override
    public Mono<Boolean> deleteQuiz(final String quizId , final String quizOwnerId) {
        return this.getQuizById(quizId).flatMap(quizOptional -> {
            if (this.isNotValid(quizOptional , quizOwnerId)) return Mono.just(false);
            final Quiz quiz = quizOptional.get();
            quiz.setIsActive(false);
            return this.kafkaService.sendQuizRecord(Mono.just(quiz)).map(Optional::isPresent);
        });
    }
}
