package com.apollo.task.kafka.processor;

import com.apollo.task.model.Quiz;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class QuizProcessor {

    @Value("${quiz.kafka.store}")
    private String quizStateStoreName;

    @Bean
    public Function<KStream<String, Quiz>, KTable<String, Quiz>> quizProcessorState() {
        return quizKStream -> quizKStream
                .groupByKey()
                .reduce((quiz , updatedQuiz) -> updatedQuiz , Materialized.as(this.quizStateStoreName));
    }

}
