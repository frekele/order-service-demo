package br.com.demo.infrastructure.api;

import br.com.demo.application.core.Pagination;
import br.com.demo.application.usecase.cancel.CancelOrderInput;
import br.com.demo.application.usecase.cancel.CancelOrderUseCase;
import br.com.demo.application.usecase.create.CreateOrderInput;
import br.com.demo.application.usecase.create.CreateOrderOutput;
import br.com.demo.application.usecase.create.CreateOrderUseCase;
import br.com.demo.application.usecase.get.GetOrderByIdInput;
import br.com.demo.application.usecase.get.GetOrderByIdOutput;
import br.com.demo.application.usecase.get.GetOrderByIdUseCase;
import br.com.demo.application.usecase.list.ListOrdersInput;
import br.com.demo.application.usecase.list.ListOrdersOutput;
import br.com.demo.application.usecase.list.ListOrdersUseCase;
import br.com.demo.application.usecase.retry.RetryOrderInput;
import br.com.demo.application.usecase.retry.RetryOrderUseCase;
import br.com.demo.domain.model.Order;
import br.com.demo.infrastructure.mapper.OrderMapper;
import br.com.demo.infrastructure.openapi.order.model.CreateOrderRequest;
import br.com.demo.infrastructure.openapi.order.model.OrderItemRequest;
import br.com.demo.infrastructure.openapi.order.model.OrderListPage;
import br.com.demo.infrastructure.openapi.order.model.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderApiDelegateImplTest {

    @Mock
    private CreateOrderUseCase createOrderUseCase;
    @Mock
    private GetOrderByIdUseCase getOrderByIdUseCase;
    @Mock
    private ListOrdersUseCase listOrdersUseCase;
    @Mock
    private CancelOrderUseCase cancelOrderUseCase;
    @Mock
    private RetryOrderUseCase retryOrderUseCase;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderApiDelegateImpl orderDelegate;

    @Captor
    private ArgumentCaptor<CreateOrderInput> createOrderInputCaptor;
    @Captor
    private ArgumentCaptor<GetOrderByIdInput> getOrderByIdInputCaptor;
    @Captor
    private ArgumentCaptor<ListOrdersInput> listOrdersInputCaptor;
    @Captor
    private ArgumentCaptor<CancelOrderInput> cancelOrderInputCaptor;
    @Captor
    private ArgumentCaptor<RetryOrderInput> retryOrderInputCaptor;

    @Test
    @DisplayName("Should create an order and return 201 Created")
    void createOrder_shouldReturnCreated() {
        var orderItemRequest = new OrderItemRequest()
                .productCode("P001")
                .productName("Product 1")
                .quantity(2)
                .unitPrice(BigDecimal.TEN);

        var createOrderRequest = new CreateOrderRequest()
                .externalOrderId("EXT-123")
                .items(List.of(orderItemRequest));

        var orderId = UUID.randomUUID();
        var createOrderOutput = new CreateOrderOutput(orderId);

        when(createOrderUseCase.execute(any(CreateOrderInput.class))).thenReturn(createOrderOutput);

        ResponseEntity<Void> response = orderDelegate.createOrder(createOrderRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(URI.create("/orders/" + orderId), response.getHeaders().getLocation());

        verify(createOrderUseCase).execute(createOrderInputCaptor.capture());
        CreateOrderInput capturedInput = createOrderInputCaptor.getValue();
        assertEquals("EXT-123", capturedInput.externalOrderId());
        assertEquals(1, capturedInput.items().size());
        assertEquals("P001", capturedInput.items().getFirst().productCode());
        assertEquals(2, capturedInput.items().getFirst().quantity());
        assertEquals(BigDecimal.TEN, capturedInput.items().getFirst().unitPrice());
    }

    @Test
    @DisplayName("Should get an order by ID and return 200 OK")
    void getOrderById_shouldReturnOk() {
        var orderId = UUID.randomUUID();
        var order = mock(Order.class);
        var getOrderByIdOutput = new GetOrderByIdOutput(order);
        var orderResponse = new OrderResponse();

        when(getOrderByIdUseCase.execute(any(GetOrderByIdInput.class))).thenReturn(getOrderByIdOutput);
        when(orderMapper.toOrderResponse(order)).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = orderDelegate.getOrderById(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderResponse, response.getBody());

        verify(getOrderByIdUseCase).execute(getOrderByIdInputCaptor.capture());
        GetOrderByIdInput capturedInput = getOrderByIdInputCaptor.getValue();
        assertEquals(orderId, capturedInput.id());

        verify(orderMapper).toOrderResponse(order);
    }

    @Test
    @DisplayName("Should list orders and return 200 OK")
    void listOrders_shouldReturnOk() {
        String status = "PENDING";
        String externalOrderId = "EXT-456";
        int page = 0;
        int size = 10;

        var pagination = new Pagination<Order>(page, size, 0L, Collections.emptyList());
        var listOrdersOutput = new ListOrdersOutput(pagination);
        var orderListPage = new OrderListPage();

        when(listOrdersUseCase.execute(any(ListOrdersInput.class))).thenReturn(listOrdersOutput);
        when(orderMapper.toOrderListPage(pagination)).thenReturn(orderListPage);

        ResponseEntity<OrderListPage> response = orderDelegate.listOrders(status, externalOrderId, page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderListPage, response.getBody());

        verify(listOrdersUseCase).execute(listOrdersInputCaptor.capture());
        ListOrdersInput capturedInput = listOrdersInputCaptor.getValue();
        assertEquals(status, capturedInput.status());
        assertEquals(externalOrderId, capturedInput.externalOrderId());
        assertEquals(page, capturedInput.page());
        assertEquals(size, capturedInput.size());

        verify(orderMapper).toOrderListPage(pagination);
    }

    @Test
    @DisplayName("Should cancel an order and return 204 No Content")
    void cancelOrder_shouldReturnNoContent() {
        var orderId = UUID.randomUUID();
        doNothing().when(cancelOrderUseCase).execute(any(CancelOrderInput.class));

        ResponseEntity<Void> response = orderDelegate.cancelOrder(orderId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(cancelOrderUseCase).execute(cancelOrderInputCaptor.capture());
        CancelOrderInput capturedInput = cancelOrderInputCaptor.getValue();
        assertEquals(orderId, capturedInput.id());
    }

    @Test
    @DisplayName("Should retry an order and return 202 Accepted")
    void retryOrder_shouldReturnAccepted() {
        var orderId = UUID.randomUUID();
        doNothing().when(retryOrderUseCase).execute(any(RetryOrderInput.class));

        ResponseEntity<Void> response = orderDelegate.retryOrder(orderId);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNull(response.getBody());

        verify(retryOrderUseCase).execute(retryOrderInputCaptor.capture());
        RetryOrderInput capturedInput = retryOrderInputCaptor.getValue();
        assertEquals(orderId, capturedInput.id());
    }
}