package br.com.demo.application.usecase.create;

import br.com.demo.application.gateway.OrderEventGateway;
import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.domain.exception.DomainException;
import br.com.demo.domain.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultCreateOrderUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private OrderEventGateway orderEventGateway;

    @InjectMocks
    private DefaultCreateOrderUseCase useCase;

    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;

    @Test
    @DisplayName("Should create an order successfully when given a valid input")
    void givenAValidInput_whenExecute_thenShouldCreateOrder() {
        final var externalOrderId = "ext-12345";
        final var itemInput = new CreateOrderInput.OrderItemInput("P01", "Product 1", 2, BigDecimal.TEN);
        final var input = new CreateOrderInput(externalOrderId, List.of(itemInput));

        when(orderGateway.findByExternalOrderId(externalOrderId)).thenReturn(Optional.empty());
        doNothing().when(orderEventGateway).sendOrderCreated(any(Order.class));

        final CreateOrderOutput output = useCase.execute(input);

        assertNotNull(output);
        assertNotNull(output.orderId());

        verify(orderGateway).findByExternalOrderId(externalOrderId);
        verify(orderEventGateway).sendOrderCreated(orderArgumentCaptor.capture());

        final Order capturedOrder = orderArgumentCaptor.getValue();
        assertNotNull(capturedOrder);
        assertEquals(externalOrderId, capturedOrder.getExternalOrderId());
        assertEquals(1, capturedOrder.getItems().size());
        assertEquals("P01", capturedOrder.getItems().getFirst().getProductCode());
    }

    @Test
    @DisplayName("Should throw DomainException when an order with the same external ID already exists")
    void givenAnExistingExternalOrderId_whenExecute_thenShouldThrowDomainException() {
        final var externalOrderId = "ext-12345";
        final var itemInput = new CreateOrderInput.OrderItemInput("P01", "Product 1", 2, BigDecimal.TEN);
        final var input = new CreateOrderInput(externalOrderId, List.of(itemInput));

        final var existingOrder = Order.builder().id(UUID.randomUUID()).externalOrderId(externalOrderId).build();

        when(orderGateway.findByExternalOrderId(externalOrderId)).thenReturn(Optional.of(existingOrder));

        final var expectedMessage = "Order with external ID " + externalOrderId + " already exists.";

        final var exception = assertThrows(DomainException.class, () -> useCase.execute(input));

        assertEquals(expectedMessage, exception.getMessage());

        verify(orderGateway).findByExternalOrderId(externalOrderId);
        verify(orderEventGateway, never()).sendOrderCreated(any(Order.class));
    }

}