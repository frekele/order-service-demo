package br.com.demo.application.usecase.get;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.domain.exception.NotFoundException;
import br.com.demo.domain.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultGetOrderByIdUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private DefaultGetOrderByIdUseCase useCase;

    private Order order;

    @BeforeEach
    void setUp() {
        order = Order.create("ext-123", List.of());
    }

    @Test
    @DisplayName("Should return an order when a valid ID is provided")
    void givenAValidId_whenCallsGetOrderById_shouldReturnOrder() {
        final var expectedId = order.getId();
        final var input = new GetOrderByIdInput(expectedId);

        when(orderGateway.findById(expectedId)).thenReturn(Optional.of(order));

        final var actualOutput = useCase.execute(input);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.order());
        assertEquals(expectedId, actualOutput.order().getId());

        verify(orderGateway, times(1)).findById(expectedId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when an invalid ID is provided")
    void givenAnInvalidId_whenCallsGetOrderById_shouldThrowNotFoundException() {
        final var expectedId = UUID.randomUUID();
        final var expectedErrorMessage = "Order with ID %s was not found".formatted(expectedId);
        final var input = new GetOrderByIdInput(expectedId);

        when(orderGateway.findById(expectedId)).thenReturn(Optional.empty());

        final var actualException = assertThrows(NotFoundException.class, () -> {
            useCase.execute(input);
        });

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(orderGateway, times(1)).findById(expectedId);
    }
}