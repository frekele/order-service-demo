package br.com.demo.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DomainExceptionTest {

    @Test
    @DisplayName("Should create DomainException and correctly set the message")
    void shouldCreateDomainExceptionWithCorrectMessage() {
        String expectedMessage = "A specific domain error occurred.";

        DomainException domainException = new DomainException(expectedMessage);

        assertThat(domainException).isInstanceOf(RuntimeException.class);
        assertThat(domainException.getMessage()).isEqualTo(expectedMessage);
    }
}