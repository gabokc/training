package dev.gabrielmumo.kc.lab.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gabrielmumo.kc.lab.kafka.domain.LibraryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class LibraryEventsProducer {

    private static final Logger log = LoggerFactory.getLogger(LibraryEventsProducer.class);

    @Value("${spring.kafka.topic}")
    public String topic;

    private final KafkaTemplate<Integer, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public LibraryEventsProducer(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    private static void accept(SendResult<Integer, String> sendResult, Throwable throwable) {
        if (throwable != null) {
            handleFailure(throwable);
        } else {
            handleSuccessful(sendResult);
        }
    }

    private static void handleFailure(Throwable throwable) {
        log.error("Error while sending the message {}", throwable.getMessage(), throwable);
    }

    private static void handleSuccessful(SendResult<Integer, String> sendResult) {
        var key = sendResult.getProducerRecord().key();
        var value = sendResult.getProducerRecord().value();
        var partition = sendResult.getRecordMetadata().partition();
        log.info("Message sent successfully for key: {} and value; {} within partition: {}", key, value, partition);
    }

    public CompletableFuture<SendResult<Integer, String>> sendLibraryEvent(LibraryEvent libraryEvent)
            throws JsonProcessingException {
        var key = libraryEvent.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);
        var completableFuture = kafkaTemplate.send(topic, key, value);
        return completableFuture
                .whenComplete(LibraryEventsProducer::accept);
    }
}
