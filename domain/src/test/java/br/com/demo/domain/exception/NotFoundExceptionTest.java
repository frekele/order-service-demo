package br.com.demo.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {

    @Test
    @DisplayName("Should correctly instantiate with a message")
    void shouldInstantiateWithMessage() {
        String expectedMessage = "Entity not found.";

        NotFoundException exception = new NotFoundException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Should be an instance of DomainException")
    void shouldBeInstanceOfDomainException() {
        assertTrue(new NotFoundException("test") instanceof DomainException);
    }
}