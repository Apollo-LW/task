package com.apollo.task.kafka.serde;

import com.apollo.task.model.Quiz;
import com.apollo.task.model.Task;
import com.apollo.task.model.TaskUser;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.jetbrains.annotations.Contract;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * Custom Serdes for the Task and User Objects
 */
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

    /**
     * {@link Quiz} Serde
     *
     * @return a {@link Quiz} Serde for the {@link Quiz} object
     */
    @Contract(" -> new ")
    public static Serde<Quiz> quizSerde() {
        return new CustomSerdes.QuizSerde();
    }

    /**
     * {@link Task} Serde
     *
     * @return a {@link Task} Serde for the {@link Task} object
     */
    @Contract(" -> new ")
    public static Serde<Task> taskSerde() {
        return new CustomSerdes.TaskSerde();
    }

    /**
     * {@link TaskUser} Serde
     *
     * @return a {@link TaskUser} Serde for the {@link TaskUser} object
     */
    @Contract(" -> new ")
    public static Serde<TaskUser> taskUserSerde() {
        return new CustomSerdes.TaskUserSerde();
    }

    static public final class QuizSerde extends Serdes.WrapperSerde<Quiz> {
        public QuizSerde() {
            super(new JsonSerializer<>() , new JsonDeserializer<>(Quiz.class));
        }
    }
}
