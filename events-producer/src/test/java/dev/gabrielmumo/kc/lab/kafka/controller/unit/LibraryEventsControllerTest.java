package dev.gabrielmumo.kc.lab.kafka.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gabrielmumo.kc.lab.kafka.controller.LibraryEventsController;
import dev.gabrielmumo.kc.lab.kafka.domain.LibraryEvent;
import dev.gabrielmumo.kc.lab.kafka.producer.LibraryEventsProducer;
import dev.gabrielmumo.kc.lab.kafka.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryEventsController.class)
public class LibraryEventsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LibraryEventsProducer libraryEventsProducer;

    @Test
    public void postLibraryEvent() throws Exception {
        var libraryEvent = TestUtil.libraryEventRecord();
        var libraryEventJSON = objectMapper.writeValueAsString(libraryEvent);
        when(libraryEventsProducer.sendLibraryEvent(isA(LibraryEvent.class)))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/libraryevent")
                        .content(libraryEventJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void postInvalidLibraryEvent() throws Exception {
        var libraryEvent = TestUtil.libraryEventRecordWithInvalidBook();
        var libraryEventJSON = objectMapper.writeValueAsString(libraryEvent);
        when(libraryEventsProducer.sendLibraryEvent(isA(LibraryEvent.class)))
                .thenReturn(null);

        var expectedValidationMessage = "book.bookId - must not be null, book.bookName - must not be blank";

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/libraryevent")
                        .content(libraryEventJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedValidationMessage));
    }
}
