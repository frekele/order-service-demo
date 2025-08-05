package br.com.demo.application.usecase.list;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListOrdersInputTest {

    @Test
    @DisplayName("Should create ListOrdersInput from parameters")
    void shouldCreateListOrdersInputFromParameters() {
        final var expectedStatus = "APPROVED";
        final var expectedExternalOrderId = "ext-order-123";
        final var expectedPage = 1;
        final var expectedSize = 20;

        final var actualInput = ListOrdersInput.from(expectedStatus, expectedExternalOrderId, expectedPage, expectedSize);

        assertNotNull(actualInput);
        assertEquals(expectedStatus, actualInput.status());
        assertEquals(expectedExternalOrderId, actualInput.externalOrderId());
        assertEquals(expectedPage, actualInput.page());
        assertEquals(expectedSize, actualInput.size());
    }
}