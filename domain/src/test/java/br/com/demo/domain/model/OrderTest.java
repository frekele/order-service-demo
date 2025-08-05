package br.com.demo.domain.model;

import br.com.demo.domain.enums.OrderStatus;
import br.com.demo.domain.exception.DomainException;
import br.com.demo.domain.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Test
    @DisplayName("Should create a new order successfully with 'RECEIVED' as initial status")
    void givenValidParams_whenCallingCreate_shouldInstantiateOrder() {
        final var expectedExternalId = "EXT-123";
        final var expectedItems = List.of(OrderItem.builder().build());

        final var actualOrder = Order.create(expectedExternalId, expectedItems);

        assertNotNull(actualOrder);
        assertNotNull(actualOrder.getId());
        assertEquals(expectedExternalId, actualOrder.getExternalOrderId());
        assertEquals(expectedItems, actualOrder.getItems());
        assertNotSame(expectedItems, actualOrder.getItems(), "Items list should be a defensive copy");
        assertEquals(OrderStatus.RECEIVED, actualOrder.getStatus());
        assertNotNull(actualOrder.getCreatedAt());
        assertNotNull(actualOrder.getUpdatedAt());
        assertNull(actualOrder.getTotalValue());
    }

    @Nested
    @DisplayName("Tests for calculateTotalValue() method")
    class CalculateTotalValueTests {

        @Test
        @DisplayName("Should calculate total value correctly for an order with multiple items")
        void givenOrderWithItems_whenCallingCalculate_shouldSetCorrectTotalValue() {
            final var item1 = OrderItem.builder().quantity(2).unitPrice(Money.of(new BigDecimal("10.00"))).build(); // 20.00
            final var item2 = OrderItem.builder().quantity(3).unitPrice(Money.of(new BigDecimal("5.50"))).build();  // 16.50
            final var order = Order.create("EXT-123", List.of(item1, item2));
            final var expectedTotal = Money.of(new BigDecimal("36.50"));

            order.calculateTotalValue();

            assertNotNull(order.getTotalValue());
            assertEquals(expectedTotal, order.getTotalValue());
        }

        @Test
        @DisplayName("Should calculate total value as zero for an order with no items")
        void givenOrderWithNoItems_whenCallingCalculate_shouldSetTotalValueToZero() {
            final var order = Order.create("EXT-123", Collections.emptyList());
            final var expectedTotal = Money.zero();

            order.calculateTotalValue();

            assertNotNull(order.getTotalValue());
            assertEquals(expectedTotal, order.getTotalValue());
        }
    }

    @Nested
    @DisplayName("Tests for the cancel() method")
    class CancelOrderTests {

        @Test
        @DisplayName("Should not cancel an order that is already completed")
        void givenACompletedOrder_whenCallingCancel_shouldThrowException() {
            final var order = Order.create("EXT-123", List.of());
            order.startProcessing();
            order.complete();
            final var expectedErrorMessage = "Cannot cancel a completed order.";

            final var exception = assertThrows(DomainException.class, order::cancel);
            assertEquals(expectedErrorMessage, exception.getMessage());
        }

        @Test
        @DisplayName("Should not cancel an order that is already cancelled")
        void givenACancelledOrder_whenCallingCancel_shouldThrowException() {
            final var order = Order.create("EXT-123", List.of());
            order.cancel();
            final var expectedErrorMessage = "Order is already cancelled.";

            final var exception = assertThrows(DomainException.class, order::cancel);
            assertEquals(expectedErrorMessage, exception.getMessage());
        }

        @Test
        @DisplayName("Should cancel a valid order successfully")
        void givenAValidOrder_whenCallingCancel_shouldUpdateStatusToCancelled() {
            final var order = Order.create("EXT-123", List.of());
            final var initialUpdatedAt = order.getUpdatedAt();

            order.cancel();

            assertEquals(OrderStatus.CANCELLED, order.getStatus());
            assertTrue(order.getUpdatedAt().isAfter(initialUpdatedAt));
        }
    }

    @Nested
    @DisplayName("Tests for the retry() method")
    class RetryOrderTests {

        @Test
        @DisplayName("Should not retry an order that is not in 'FAILED' state")
        void givenANonFailedOrder_whenCallingRetry_shouldThrowException() {
            final var order = Order.create("EXT-123", List.of()); // Status is RECEIVED
            final var expectedErrorMessage = "Only FAILED orders can be retried.";

            final var exception = assertThrows(DomainException.class, order::retry);

            assertEquals(expectedErrorMessage, exception.getMessage());
        }

        @Test
        @DisplayName("Should retry a failed order, changing its status to 'RECEIVED'")
        void givenAFailedOrder_whenCallingRetry_shouldUpdateStatusToReceived() {
            final var order = Order.create("EXT-123", List.of());
            order.fail();
            final var initialUpdatedAt = order.getUpdatedAt();

            order.retry();

            assertEquals(OrderStatus.RECEIVED, order.getStatus());
            assertTrue(order.getUpdatedAt().isAfter(initialUpdatedAt));
        }
    }

    @Nested
    @DisplayName("Tests for state transitions")
    class StateTransitionTests {

        @Test
        @DisplayName("Should transition from RECEIVED to PROCESSING")
        void givenReceivedOrder_whenStartProcessing_shouldUpdateStatusToProcessing() {
            final var order = Order.create("EXT-123", List.of());
            final var initialUpdatedAt = order.getUpdatedAt();

            order.startProcessing();

            assertEquals(OrderStatus.PROCESSING, order.getStatus());
            assertTrue(order.getUpdatedAt().isAfter(initialUpdatedAt));
        }

        @Test
        @DisplayName("Should not start processing an order that is not in 'RECEIVED' state")
        void givenANonReceivedOrder_whenCallingStartProcessing_shouldThrowException() {
            final var order = Order.create("EXT-123", List.of());
            order.startProcessing(); // Now it's PROCESSING
            final var expectedErrorMessage = "Order must be in RECEIVED state to start processing.";

            final var exception = assertThrows(DomainException.class, order::startProcessing);
            assertEquals(expectedErrorMessage, exception.getMessage());
        }

        @Test
        @DisplayName("Should transition from PROCESSING to COMPLETED")
        void givenProcessingOrder_whenComplete_shouldUpdateStatusToCompleted() {
            final var order = Order.create("EXT-123", List.of());
            order.startProcessing();
            final var initialUpdatedAt = order.getUpdatedAt();

            order.complete();

            assertEquals(OrderStatus.COMPLETED, order.getStatus());
            assertTrue(order.getUpdatedAt().isAfter(initialUpdatedAt));
        }

        @Test
        @DisplayName("Should not complete an order that is not in 'PROCESSING' state")
        void givenANonProcessingOrder_whenCallingComplete_shouldThrowException() {
            final var order = Order.create("EXT-123", List.of()); // It's in RECEIVED state
            final var expectedErrorMessage = "Order must be in PROCESSING state to be completed.";

            final var exception = assertThrows(DomainException.class, order::complete);
            assertEquals(expectedErrorMessage, exception.getMessage());
        }

        @Test
        @DisplayName("Should transition to FAILED status")
        void givenAnyOrder_whenFail_shouldUpdateStatusToFailed() {
            final var order = Order.create("EXT-123", List.of());
            final var initialUpdatedAt = order.getUpdatedAt();

            order.fail();

            assertEquals(OrderStatus.FAILED, order.getStatus());
            assertTrue(order.getUpdatedAt().isAfter(initialUpdatedAt));
        }
    }
}