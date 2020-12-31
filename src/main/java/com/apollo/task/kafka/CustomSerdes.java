package com.apollo.task.kafka;

import com.apollo.task.model.Task;
import com.apollo.task.model.TaskUser;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.jetbrains.annotations.Contract;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CustomSerdes {

    static public final class TaskSerde extends Serdes.WrapperSerde<Task> {
        public TaskSerde() {
            super(new JsonSerializer<>() , new JsonDeserializer<>(Task.class));
        }
    }

    static public final class TaskUserSerde extends Serdes.WrapperSerde<TaskUser> {
        public TaskUserSerde() {
            super(new JsonSerializer<>() , new JsonDeserializer<>(TaskUser.class));
        }
    }

    @Contract(" -> new ")
    public static Serde<Task> taskSerde() {
        return new CustomSerdes.TaskSerde();
    }

    @Contract(" -> new ")
    public static Serde<TaskUser> taskUserSerde() {
        return new CustomSerdes.TaskUserSerde();
    }
}
