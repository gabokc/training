package dev.gabrielmumo.kc.lab.kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.gabrielmumo.kc.lab.kafka.domain.LibraryEvent;
import dev.gabrielmumo.kc.lab.kafka.domain.LibraryEventType;
import dev.gabrielmumo.kc.lab.kafka.producer.LibraryEventsProducer;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class LibraryEventsController {

    private static final Logger log = LoggerFactory.getLogger(LibraryEventsController.class);

    private final LibraryEventsProducer libraryEventsProducer;

    public LibraryEventsController(LibraryEventsProducer libraryEventsProducer) {
        this.libraryEventsProducer = libraryEventsProducer;
    }

    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent)
            throws JsonProcessingException {
        log.info("post libraryEvent: {}", libraryEvent);
        libraryEventsProducer.sendLibraryEvent(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PutMapping("/v1/libraryevent")
    public ResponseEntity<?> updateLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent)
            throws JsonProcessingException {
        log.info("update libraryEvent: {}", libraryEvent);

        var BAD_REQUEST = checkLibraryEvent(libraryEvent);
        if (BAD_REQUEST.isPresent()) return BAD_REQUEST.get();

        libraryEventsProducer.sendLibraryEvent(libraryEvent);
        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
    }

    private static Optional<ResponseEntity<String>> checkLibraryEvent(LibraryEvent libraryEvent) {
        if(libraryEvent.libraryEventId() == null) {
            return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Event id is required"));
        }

        if(libraryEvent.libraryEventType() != LibraryEventType.UPDATE) {
            return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Only update event type is supported"));
        }
        return Optional.empty();
    }
}
