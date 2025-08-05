package br.com.demo.infrastructure.queue;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.application.usecase.process.ProcessOrderInput;
import br.com.demo.application.usecase.process.ProcessOrderUseCase;
import br.com.demo.domain.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderEventListenerTest {

    @InjectMocks
    private OrderEventListener orderEventListener;

    @Mock
    private ProcessOrderUseCase processOrderUseCase;

    @Mock
    private OrderGateway orderGateway;

    @Test
    @DisplayName("Should process order successfully when a valid order is received")
    void onOrderCreated_whenOrderIsValid_shouldProcessSuccessfully() {
        final var order = Order.create("external-id-123", new ArrayList<>());

        orderEventListener.onOrderCreated(order);

        verify(processOrderUseCase).execute(any(ProcessOrderInput.class));
        verifyNoInteractions(orderGateway);
    }

    @Test
    @DisplayName("Should fail order and throw exception when an error occurs during processing")
    void onOrderCreated_whenProcessingFails_shouldFailOrderAndThrow() {
        final var order = spy(Order.create("external-id-456", new ArrayList<>()));
        final var expectedException = new RuntimeException("Critical processing error");

        doThrow(expectedException)
                .when(processOrderUseCase).execute(any(ProcessOrderInput.class));

        final var actualException = assertThrows(AmqpRejectAndDontRequeueException.class, () -> {
            orderEventListener.onOrderCreated(order);
        });

        assertEquals(expectedException, actualException.getCause());

        verify(processOrderUseCase).execute(any(ProcessOrderInput.class));
        verify(order).fail();
        verify(orderGateway).save(order);
    }
}