package dev.gabrielmumo.kc.lab.kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.gabrielmumo.kc.lab.kafka.domain.LibraryEvent;
import dev.gabrielmumo.kc.lab.kafka.producer.LibraryEventsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibraryEventsController {

    private static final Logger log = LoggerFactory.getLogger(LibraryEventsController.class);

    private final LibraryEventsProducer libraryEventsProducer;

    public LibraryEventsController(LibraryEventsProducer libraryEventsProducer) {
        this.libraryEventsProducer = libraryEventsProducer;
    }

    @RequestMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent)
            throws JsonProcessingException {
        log.info("libraryEvent: {}", libraryEvent);
        libraryEventsProducer.sendLibraryEvent(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
