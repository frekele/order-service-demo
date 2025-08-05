package br.com.demo.application.usecase.get;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GetOrderByIdInputTest {

    @Test
    @DisplayName("Should create GetOrderByIdInput and get its value")
    void shouldCreateInputAndGetValue() {
        final var expectedId = UUID.randomUUID();

        final var input = new GetOrderByIdInput(expectedId);

        assertNotNull(input);
        assertEquals(expectedId, input.id());
    }
}