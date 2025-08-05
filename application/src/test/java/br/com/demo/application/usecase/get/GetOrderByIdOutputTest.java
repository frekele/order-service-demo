package br.com.demo.application.usecase.get;

import br.com.demo.domain.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GetOrderByIdOutputTest {

    @Test
    @DisplayName("Should create GetOrderByIdOutput from an Order")
    void shouldCreateGetOrderByIdOutputFromOrder() {
        final var order = Order.builder()
                .id(UUID.randomUUID())
                .build();

        final var output = GetOrderByIdOutput.from(order);

        assertNotNull(output);
        assertEquals(order, output.order());
    }
}