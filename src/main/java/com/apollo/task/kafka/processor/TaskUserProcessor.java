package com.apollo.task.kafka.processor;

import com.apollo.task.kafka.serde.CustomSerdes;
import com.apollo.task.model.Task;
import com.apollo.task.model.TaskUser;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The aggregation of tasks for each user Processor
 */
@Service
public class TaskUserProcessor {

    /**
     * User task topic name
     */
    @Value("${user.kafka.store}")
    private String taskUserStateStoreName;

    /**
     * Aggregating the tasks for each user from the {@link KStream}
     *
     * @return a {@link KTable} of {@link TaskUser} which is the join between the task and the user, with the userId as the key
     */
    @Bean
    public Function<KStream<String, Task>, KTable<String, TaskUser>> taskUserProcessorState() {
        return taskKStream -> taskKStream
                .flatMap((taskId , task) -> task
                        .getAllTaskMembers()
                        .stream()
                        .map(memberId -> new KeyValue<String, Task>(memberId , task)).collect(Collectors.toSet()))
                .groupByKey(Grouped.with(Serdes.String() , CustomSerdes.taskSerde()))
                .aggregate(TaskUser::new ,
                        (memberId , task , taskUser) -> {
                            taskUser.addUserTaskByType(task.getTaskType() , task);
                            taskUser.addUserTaskByStatus(task.getTaskStatus() , task);
                            taskUser.addUserTaskByGroupName(task.getTaskGroupName() , task);
                            return taskUser;
                        } , Materialized.with(Serdes.String() , CustomSerdes.taskUserSerde()))
                .toStream()
                .groupByKey(Grouped.with(Serdes.String() , CustomSerdes.taskUserSerde()))
                .reduce((taskUser , updatedTaskUser) -> updatedTaskUser , Materialized.as(this.taskUserStateStoreName));
    }

}
