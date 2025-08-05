package br.com.demo.application.usecase.create;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class CreateOrderOutputTest {

    @Test
    void givenAValidId_whenCallingFrom_shouldInstantiateACreateOrderOutput() {
        final var expectedOrderId = UUID.randomUUID();

        final var actualOutput = CreateOrderOutput.from(expectedOrderId);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedOrderId, actualOutput.orderId());
    }

    @Test
    void testRecordProperties() {
        final var expectedOrderId = UUID.randomUUID();
        final var output = new CreateOrderOutput(expectedOrderId);
        Assertions.assertEquals(expectedOrderId, output.orderId());
    }
}