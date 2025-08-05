package br.com.demo.application.usecase.cancel;

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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultCancelOrderUseCaseTest {

    @InjectMocks
    private DefaultCancelOrderUseCase useCase;

    @Mock
    private OrderGateway orderGateway;

    // Using a Spy to allow calling real methods on the Order object
    // while still being able to verify interactions.
    @Spy
    private Order order = Order.create("external-id-123", Collections.emptyList());

    @Test
    @DisplayName("Should cancel order successfully when given a valid and cancellable order ID")
    void givenValidInput_whenCancellingOrder_shouldCancelAndSave() {
        // Arrange
        final var orderId = order.getId();
        final var input = new CancelOrderInput(orderId);

        when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));
        when(orderGateway.save(any(Order.class))).thenReturn(order);

        // Act
        assertDoesNotThrow(() -> useCase.execute(input));

        // Assert
        verify(orderGateway, times(1)).findById(orderId);
        verify(order, times(1)).cancel();
        verify(orderGateway, times(1)).save(order);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    @DisplayName("Should throw NotFoundException when order ID does not exist")
    void givenInvalidOrderId_whenCancellingOrder_shouldThrowNotFoundException() {
        // Arrange
        final var nonExistentId = UUID.randomUUID();
        final var input = new CancelOrderInput(nonExistentId);
        final var expectedErrorMessage = "Order with ID %s was not found".formatted(nonExistentId);

        when(orderGateway.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(orderGateway, times(1)).findById(nonExistentId);
        verify(orderGateway, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw DomainException when trying to cancel a completed order")
    void givenCompletedOrder_whenCancellingOrder_shouldThrowDomainException() {
        // Arrange
        // Put order in a state that cannot be cancelled
        order.startProcessing();
        order.complete();

        final var orderId = order.getId();
        final var input = new CancelOrderInput(orderId);
        final var expectedErrorMessage = "Cannot cancel a completed order.";

        when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        final var exception = assertThrows(DomainException.class, () -> useCase.execute(input));

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(orderGateway, times(1)).findById(orderId);
        // The cancel method is called, but it throws the exception internally
        verify(order, times(1)).cancel();
        verify(orderGateway, never()).save(any(Order.class));
    }
}