package dev.gabrielmumo.kc.lab.kafka.domain;

public record LibraryEvent(Integer libraryEventId, LibraryEventType libraryEventType, Book book) {
}
