package com.apollo.task.kafka.processor;

import com.apollo.task.model.Task;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TaskProcessor {

    @Value("${task.kafka.store}")
    private String taskStateStoreName;

    @Bean
    public Function<KStream<String, Task>, KTable<String, Task>> taskProcessorState() {
        return taskKStream -> taskKStream
                .groupByKey()
                .reduce((task , updatedTask) -> updatedTask , Materialized.as(this.taskStateStoreName));
    }

}
