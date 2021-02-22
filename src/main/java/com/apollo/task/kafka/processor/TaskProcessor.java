package com.apollo.task.kafka.processor;

import com.apollo.task.model.Task;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * Main Task Processor that handle basic final state operations for the quiz
 */
@Service
public class TaskProcessor {

    /**
     * Main Task Topic Name
     */
    @Value("${task.kafka.store}")
    private String taskStateStoreName;

    /**
     * Updating the task event into it's final state from the {@link KStream}
     *
     * @return a {@link KTable} that have the final state of the events with the {@link Task#getTaskId()} as the key
     */
    @Bean
    public Function<KStream<String, Task>, KTable<String, Task>> taskProcessorState() {
        return taskKStream -> taskKStream
                .groupByKey()
                .reduce((task , updatedTask) -> updatedTask , Materialized.as(this.taskStateStoreName));
    }

}
