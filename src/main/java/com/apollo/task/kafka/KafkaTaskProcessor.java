package com.apollo.task.kafka;

import com.apollo.task.model.Quiz;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KafkaTaskProcessor {

    @Value("${quiz.kafka.store}")
    String quizStateStoreName;
    @Value("${user.kafka.store}")
    private String taskUserStateStoreName;

    @Bean
    public Function<KStream<String , Quiz>, KTable<String , Quiz>> quizProcessor() {
       return null;
    }
}
