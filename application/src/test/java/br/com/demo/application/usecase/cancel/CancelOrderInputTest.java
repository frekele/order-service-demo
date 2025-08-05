package br.com.demo.application.usecase.cancel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class CancelOrderInputTest {

    @Test
    @DisplayName("Should create input and get the correct id")
    void shouldCreateInputAndGetId() {
        var expectedId = UUID.randomUUID();

        var input = new CancelOrderInput(expectedId);

        assertThat(input.id()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Should be equal for two instances with the same id")
    void shouldBeEqualForSameId() {
        var id = UUID.randomUUID();
        var input1 = new CancelOrderInput(id);
        var input2 = new CancelOrderInput(id);

        assertThat(input1).isEqualTo(input2);
        assertThat(input1.hashCode()).isEqualTo(input2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal for two instances with different ids")
    void shouldNotBeEqualForDifferentIds() {
        var input1 = new CancelOrderInput(UUID.randomUUID());
        var input2 = new CancelOrderInput(UUID.randomUUID());

        assertThat(input1).isNotEqualTo(input2);
    }

    @Test
    @DisplayName("Should have a correct toString representation")
    void shouldHaveCorrectToString() {
        var id = UUID.randomUUID();
        var input = new CancelOrderInput(id);

        assertThat(input.toString()).isEqualTo("CancelOrderInput[id=" + id + "]");
    }
}