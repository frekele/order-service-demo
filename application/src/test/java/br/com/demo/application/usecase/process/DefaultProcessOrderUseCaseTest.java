package br.com.demo.application.usecase.process;

import br.com.demo.application.gateway.NotificationGateway;
import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.domain.exception.DomainException;
import br.com.demo.domain.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultProcessOrderUseCaseTest {

    @InjectMocks
    private DefaultProcessOrderUseCase useCase;

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private NotificationGateway notificationGateway;

    @Test
    @DisplayName("Should process order successfully when given a valid order")
    void shouldProcessOrderSuccessfully() {
        final var order = Order.create("ext-123", Collections.emptyList());
        final var orderSpy = spy(order);
        final var input = new ProcessOrderInput(orderSpy);

        useCase.execute(input);

        final InOrder inOrder = inOrder(orderSpy, orderGateway, notificationGateway);

        inOrder.verify(orderSpy).startProcessing();
        inOrder.verify(orderGateway).save(orderSpy);
        inOrder.verify(orderSpy).calculateTotalValue();
        inOrder.verify(orderSpy).complete();
        inOrder.verify(notificationGateway).notifyOrderCompleted(orderSpy);
        inOrder.verify(orderGateway).save(orderSpy);

        verifyNoMoreInteractions(orderGateway, notificationGateway);
    }

    @Test
    @DisplayName("Should throw DomainException when order is not in RECEIVED state")
    void shouldThrowDomainExceptionWhenOrderNotInReceivedState() {
        final var order = Order.create("ext-123", Collections.emptyList());
        order.startProcessing();
        final var input = new ProcessOrderInput(order);

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(input));

        assertEquals("Order must be in RECEIVED state to start processing.", exception.getMessage());

        verifyNoInteractions(orderGateway);
        verifyNoInteractions(notificationGateway);
    }

    @Test
    @DisplayName("Should not proceed if first save fails")
    void shouldNotProceedIfFirstSaveFails() {
        final var order = Order.create("ext-123", Collections.emptyList());
        final var orderSpy = spy(order);
        final var input = new ProcessOrderInput(orderSpy);
        final var expectedException = new RuntimeException("Database connection failed");

        doThrow(expectedException).when(orderGateway).save(orderSpy);

        final var actualException = assertThrows(RuntimeException.class, () -> useCase.execute(input));

        assertEquals(expectedException, actualException);

        final InOrder inOrder = inOrder(orderSpy, orderGateway);
        inOrder.verify(orderSpy).startProcessing();
        inOrder.verify(orderGateway).save(orderSpy);

        verify(orderSpy, never()).calculateTotalValue();
        verify(orderSpy, never()).complete();
        verifyNoInteractions(notificationGateway);
    }
}