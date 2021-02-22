package com.apollo.task.kafka.processor;

import com.apollo.task.model.Quiz;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * Main Quiz Processor that handle basic final state operations for the quiz
 */
@Service
public class QuizProcessor {

    /**
     * Main Quiz Topic Name
     */
    @Value("${quiz.kafka.store}")
    private String quizStateStoreName;

    /**
     * Updating the quiz event into it's final state from the {@link KStream}
     *
     * @return a {@link KTable} that have the final state of the events with the {@link Quiz#getQuizId()} as the key
     */
    @Bean
    public Function<KStream<String, Quiz>, KTable<String, Quiz>> quizProcessorState() {
        return quizKStream -> quizKStream
                .groupByKey()
                .reduce((quiz , updatedQuiz) -> updatedQuiz , Materialized.as(this.quizStateStoreName));
    }

}
