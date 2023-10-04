package dev.gabrielmumo.kc.lab.kafka.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gabrielmumo.kc.lab.kafka.domain.Book;
import dev.gabrielmumo.kc.lab.kafka.domain.LibraryEvent;
import dev.gabrielmumo.kc.lab.kafka.domain.LibraryEventType;

public class TestUtil {

    public static Book bookRecord(){
        return new Book(123, "Kafka Using Spring Boot","Dilip" );
    }

    public static Book bookRecordWithInvalidValues(){
        return new Book(null, "","Dilip" );
    }

    public static LibraryEvent libraryEventRecord(){
        return new LibraryEvent(null, LibraryEventType.NEW, bookRecord());
    }

    public static LibraryEvent libraryEventRecordWithInvalidBook(){
        return new LibraryEvent(null, LibraryEventType.NEW, bookRecordWithInvalidValues());
    }

    public static LibraryEvent parseLibraryEventRecord(ObjectMapper objectMapper , String json){
        try {
            return  objectMapper.readValue(json, LibraryEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
