package com.apollo.task.kafka;

import com.apollo.task.model.Quiz;
import com.apollo.task.model.Task;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KafkaService {

    @Value("${task.kafka.topic}")
    private String taskTopicName;
    private final KafkaSender<String, Task> taskKafkaSender;

    @Value("${quiz.kafka.topic}")
    private String quizTopicName;
    private final KafkaSender<String, Quiz> quizKafkaSender;


    public Mono<Optional<Task>> sendTaskRecord(Mono<Task> taskMono) {
        return taskMono.flatMap(task -> this.taskKafkaSender
                .send(Mono.just(SenderRecord.create(new ProducerRecord<String, Task>(this.taskTopicName , task.getTaskId() , task) , task.getTaskId())))
                .next()
                .map(senderResult -> senderResult.exception() == null ? Optional.of(task) : Optional.empty()));
    }

    public Mono<Optional<Quiz>> sendQuizRecord(Mono<Quiz> quizMono) {
        return quizMono.flatMap(quiz -> this.quizKafkaSender
                .send(Mono.just(SenderRecord.create(new ProducerRecord<String, Quiz>(this.quizTopicName , quiz.getQuizId() , quiz) , quiz.getQuizId())))
                .next()
                .map(senderResult -> senderResult.exception() == null ? Optional.of(quiz) : Optional.empty()));
    }
}
