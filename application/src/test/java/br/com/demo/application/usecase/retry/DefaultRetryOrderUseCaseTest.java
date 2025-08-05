package br.com.demo.application.usecase.retry;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.domain.enums.OrderStatus;
import br.com.demo.domain.exception.DomainException;
import br.com.demo.domain.exception.NotFoundException;
import br.com.demo.domain.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultRetryOrderUseCaseTest {

    @InjectMocks
    private DefaultRetryOrderUseCase useCase;

    @Mock
    private OrderGateway orderGateway;

    @Test
    @DisplayName("Should retry order successfully when order is in FAILED state")
    void shouldRetryOrderSuccessfully() {
        final var orderId = UUID.randomUUID();
        final var input = new RetryOrderInput(orderId);

        final Order failedOrder = Order.builder()
                .id(orderId)
                .externalOrderId("ext-123")
                .items(Collections.emptyList())
                .status(OrderStatus.FAILED) // Initial state must be FAILED
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusHours(1))
                .build();

        when(orderGateway.findById(orderId)).thenReturn(Optional.of(failedOrder));

        final var result = useCase.execute(input);

        assertNull(result);
        assertEquals(OrderStatus.RECEIVED, failedOrder.getStatus());
        assertNotNull(failedOrder.getUpdatedAt());

        verify(orderGateway).findById(orderId);
        verify(orderGateway).save(failedOrder);
    }

    @Test
    @DisplayName("Should throw NotFoundException when order does not exist")
    void shouldThrowNotFoundExceptionWhenOrderDoesNotExist() {
        final var orderId = UUID.randomUUID();
        final var input = new RetryOrderInput(orderId);
        final var expectedMessage = "Order with ID %s was not found".formatted(orderId);

        when(orderGateway.findById(orderId)).thenReturn(Optional.empty());

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        assertEquals(expectedMessage, exception.getMessage());
        verify(orderGateway).findById(orderId);
        verify(orderGateway, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw DomainException when trying to retry an order that is not in FAILED state")
    void shouldThrowDomainExceptionWhenOrderIsNotFailed() {
        final var orderId = UUID.randomUUID();
        final var input = new RetryOrderInput(orderId);
        final var expectedMessage = "Only FAILED orders can be retried.";

        final Order completedOrder = Order.builder().id(orderId).status(OrderStatus.COMPLETED).build();

        when(orderGateway.findById(orderId)).thenReturn(Optional.of(completedOrder));

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(input));

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(OrderStatus.COMPLETED, completedOrder.getStatus()); // Status should not change

        verify(orderGateway).findById(orderId);
        verify(orderGateway, never()).save(any(Order.class));
    }
}