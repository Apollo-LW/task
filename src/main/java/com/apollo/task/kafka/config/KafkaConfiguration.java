package com.apollo.task.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.internals.DefaultKafkaSender;
import reactor.kafka.sender.internals.ProducerFactory;

import java.util.Collections;
import java.util.Properties;

/**
 * Main Kafka Configuration for the producer {@link KafkaSender} and the consumer {@link KafkaReceiver}
 */
public class KafkaConfiguration {

    /**
     * Kafka Configuration constants
     */
    @Value("${task.kafka.server}")
    private String bootstrapServer;
    @Value("${task.kafka.topic}")
    private String taskTopicName;
    @Value("${quiz.kafka.topic}")
    private String quizTopicName;
    @Value("${task.kafka.partition}")
    private Integer numberOfPartitions;
    @Value("${task.kafka.replicas}")
    private Short numberOfReplicas;
    @Value("${task.kafka.retention}")
    private String retentionPeriod;
    @Value("${task.kafka.acks}")
    private String numberOfAcks;
    @Value("${task.kafka.retries}")
    private Integer numberOfRetries;
    @Value("${task.kafka.requestTimeOut}")
    private String requestTimeout;
    @Value("${task.kafka.batch}")
    private String batchSize;
    @Value("${task.kafka.linger}")
    private String linger;
    @Value("${task.kafka.max-in-flight}")
    private String maxInFlight;
    @Value("${task.kafka.client-id}")
    private String clientId;
    @Value("${task.kafka.group-id}")
    private String groupId;
    @Value("${task.kafka.offset}")
    private String offset;

    /**
     * Create the main Task Topic in Kafka
     *
     * @return a {@link NewTopic} that is created
     */
    @Bean
    public NewTopic createTaskTopic() {
        return TopicBuilder
                .name(this.taskTopicName)
                .partitions(this.numberOfPartitions)
                .replicas(this.numberOfReplicas)
                .config(TopicConfig.RETENTION_MS_CONFIG , this.retentionPeriod)
                .build();
    }

    /**
     * Create the main Quiz Topic in Kafka
     *
     * @return a {@link NewTopic} that is created
     */
    @Bean
    public NewTopic createQuizTopic() {
        return TopicBuilder
                .name(this.quizTopicName)
                .partitions(this.numberOfPartitions)
                .replicas(this.numberOfReplicas)
                .config(TopicConfig.RETENTION_MS_CONFIG , this.retentionPeriod)
                .build();
    }

    /**
     * Create a reactive Kafka Producer
     *
     * @return a generic configured reactive Kafka Producer {@link KafkaSender}
     */
    @Bean
    KafkaSender taskKafkaSender() {
        final Properties taskSenderProperties = new Properties();
        taskSenderProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG , this.bootstrapServer);
        taskSenderProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG , StringSerializer.class);
        taskSenderProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG , JsonSerializer.class);
        taskSenderProperties.put(ProducerConfig.ACKS_CONFIG , this.numberOfAcks);
        taskSenderProperties.put(ProducerConfig.RETRIES_CONFIG , this.numberOfRetries);
        taskSenderProperties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG , this.requestTimeout);
        taskSenderProperties.put(ProducerConfig.BATCH_SIZE_CONFIG , this.batchSize);
        taskSenderProperties.put(ProducerConfig.LINGER_MS_CONFIG , this.linger);
        taskSenderProperties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION , this.maxInFlight);

        return new DefaultKafkaSender(ProducerFactory.INSTANCE , SenderOptions.create(taskSenderProperties));
    }

    /**
     * Create a reactive Kafka Consumer
     *
     * @return a generic configured reactive Kafka Consumer {@link KafkaReceiver}
     */
    @Bean
    KafkaReceiver taskKafkaReceiver() {
        final Properties taskReceiverProperties = new Properties();
        taskReceiverProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG , this.bootstrapServer);
        taskReceiverProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG , StringDeserializer.class);
        taskReceiverProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG , JsonDeserializer.class);
        taskReceiverProperties.put(ConsumerConfig.CLIENT_ID_CONFIG , this.clientId);
        taskReceiverProperties.put(ConsumerConfig.GROUP_ID_CONFIG , this.groupId);
        taskReceiverProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG , true);
        taskReceiverProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG , this.offset);

        return new DefaultKafkaReceiver(ConsumerFactory.INSTANCE , ReceiverOptions.create(taskReceiverProperties).subscription(Collections.singleton(this.taskTopicName)));
    }

}
