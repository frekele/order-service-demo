package br.com.demo.application.usecase.retry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RetryOrderInputTest {

    @Test
    @DisplayName("Should create RetryOrderInput and get id successfully")
    void shouldCreateRetryOrderInputAndGetId() {
        var expectedId = UUID.randomUUID();

        var input = new RetryOrderInput(expectedId);

        assertNotNull(input);
        assertEquals(expectedId, input.id());
    }
}