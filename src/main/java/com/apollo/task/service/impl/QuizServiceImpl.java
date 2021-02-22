package com.apollo.task.service.impl;

import com.apollo.task.kafka.service.KafkaService;
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

/**
 * Main {@link QuizService} implementation to deal with events and kafka
 */
@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    /**
     * {@link InteractiveQueryService} to get the Quiz state store
     */
    private final InteractiveQueryService interactiveQueryService;
    /**
     * {@link KafkaService} to produce events
     */
    private final KafkaService kafkaService;
    /**
     * Quiz Topic name
     */
    @Value("${quiz.kafka.store}")
    private String quizStateStoreName;
    /**
     * Main Quiz State Store
     */
    private ReadOnlyKeyValueStore<String, Quiz> quizStateStore;

    /**
     * a to method to get the {@link ReadOnlyKeyValueStore} from the {@link InteractiveQueryService} so it can be accessed
     *
     * @return a {@link ReadOnlyKeyValueStore} which is the Quiz State Store
     */
    private ReadOnlyKeyValueStore<String, Quiz> getQuizStateStore() {
        if (this.quizStateStore == null)
            this.quizStateStore = interactiveQueryService.getQueryableStore(this.quizStateStoreName , QueryableStoreTypes.keyValueStore());
        return this.quizStateStore;
    }

    /**
     * Checking if a quiz is valid to be returned or processed
     *
     * @param quizOptional the quiz object to check, it's is an {@link Optional} so we can handle null checks
     * @param quizOwnerId  one of the quiz object owners {@link Quiz#getQuizOwnerId()} to check
     *
     * @return true if and only if the quiz object is not null, and the owner is valid
     */
    private boolean isNotValid(final Optional<Quiz> quizOptional , final String quizOwnerId) {
        return quizOptional.isEmpty() || !quizOptional.get().getQuizOwnerId().equals(quizOwnerId);
    }

    /**
     * Get a {@link Quiz} in it's final state from the state store
     *
     * @param quizId the quiz id to find in the state store
     *
     * @return an {@link Optional} of a {@link Quiz}
     */
    @Override
    public Mono<Optional<Quiz>> getQuizById(final String quizId) {
        return Mono.just(Optional.ofNullable(this.getQuizStateStore().get(quizId)));
    }

    /**
     * Produce a quiz event to Kafka
     *
     * @param quizMono the quiz to proudce
     *
     * @return the produced quiz object wrapped in {@link Optional}
     */
    @Override
    public Mono<Optional<Quiz>> saveQuiz(final Mono<Quiz> quizMono) {
        return this.kafkaService.sendQuizRecord(quizMono);
    }

    /**
     * Produce an updated quiz event to kafka
     *
     * @param quizMono    the updated quiz
     * @param quizOwnerId one of the quiz owners, it must be valid in the quiz before the update
     *
     * @return true if and only if the quiz was updated successfully, and false otherwise
     */
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

    /**
     * Disable a quiz
     *
     * @param quizId      the quiz to be deleted
     * @param quizOwnerId one of the quiz owners ID
     *
     * @return true if and only if the quiz was deleted successfully, and false otherwise
     */
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
