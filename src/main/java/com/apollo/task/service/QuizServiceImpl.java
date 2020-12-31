package com.apollo.task.service;

import com.apollo.task.kafka.KafkaService;
import com.apollo.task.model.Quiz;
import com.apollo.task.model.Task;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private QuizService quizService;

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

    @Override
    public Mono<Optional<Quiz>> getQuizById(String quizId) {
        return Mono.just(Optional.ofNullable(this.getQuizStateStore().get(quizId)));
    }

    @Override
    public Mono<Quiz> postQuiz(Mono<Quiz> quizMono) {
        return this.kafkaService.sendQuizRecord(quizMono).map(Optional::get);
    }

    @Override
    public Mono<Quiz> updateQuiz(Mono<Quiz> quizMono) {
        return null;
    }

    @Override
    public Mono<Boolean> deleteQuiz(String quizId) {
        return null;
    }
}
