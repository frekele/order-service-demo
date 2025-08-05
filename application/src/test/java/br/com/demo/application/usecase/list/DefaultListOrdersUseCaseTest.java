package br.com.demo.application.usecase.list;

import br.com.demo.application.core.Pagination;
import br.com.demo.application.core.SearchQuery;
import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.domain.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultListOrdersUseCaseTest {

    @InjectMocks
    private DefaultListOrdersUseCase defaultListOrdersUseCase;

    @Mock
    private OrderGateway orderGateway;

    @Test
    void givenAValidQuery_whenCallsListOrders_thenShouldReturnPaginationResult() {
        final var expectedPage = 0;
        final var expectedSize = 10;
        final var expectedStatus = "APPROVED";
        final var expectedExternalOrderId = "ext-order-id-123";
        final var expectedOrders = List.of(mock(Order.class));

        final var aPagination = new Pagination<>(expectedPage, expectedSize, 1L, expectedOrders);

        when(orderGateway.findAll(any(SearchQuery.class)))
                .thenReturn(aPagination);

        final var anInput = ListOrdersInput.from(expectedStatus, expectedExternalOrderId, expectedPage, expectedSize);

        final var actualOutput = defaultListOrdersUseCase.execute(anInput);

        assertNotNull(actualOutput);
        assertEquals(aPagination, actualOutput.pagination());

        final var searchQueryCaptor = ArgumentCaptor.forClass(SearchQuery.class);
        verify(orderGateway, times(1)).findAll(searchQueryCaptor.capture());

        final var capturedQuery = searchQueryCaptor.getValue();
        assertEquals(expectedPage, capturedQuery.page());
        assertEquals(expectedSize, capturedQuery.perPage());
        assertEquals(expectedStatus, capturedQuery.status());
        assertEquals(expectedExternalOrderId, capturedQuery.externalOrderId());
    }

    @Test
    void givenAValidQuery_whenGatewayReturnsEmptyList_thenShouldReturnEmptyPaginationResult() {
        final var expectedPage = 0;
        final var expectedSize = 10;
        final var expectedOrders = List.<Order>of();
        final var aPagination = new Pagination<>(expectedPage, expectedSize, 0L, expectedOrders);
        when(orderGateway.findAll(any())).thenReturn(aPagination);

        final var anInput = ListOrdersInput.from("PENDING", "ext-456", expectedPage, expectedSize);

        final var actualOutput = defaultListOrdersUseCase.execute(anInput);

        assertNotNull(actualOutput);
        assertEquals(0, actualOutput.pagination().items().size());
        assertEquals(aPagination, actualOutput.pagination());
        verify(orderGateway, times(1)).findAll(any(SearchQuery.class));
    }
}