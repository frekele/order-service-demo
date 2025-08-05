package br.com.demo.infrastructure.api;

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
import br.com.demo.infrastructure.mapper.OrderMapper;
import br.com.demo.infrastructure.openapi.order.api.OrdersApiDelegate;
import br.com.demo.infrastructure.openapi.order.model.CreateOrderRequest;
import br.com.demo.infrastructure.openapi.order.model.OrderListPage;
import br.com.demo.infrastructure.openapi.order.model.OrderResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class OrderDelegateImpl implements OrdersApiDelegate {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final ListOrdersUseCase listOrdersUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final RetryOrderUseCase retryOrderUseCase;
    private final OrderMapper orderMapper;

    @Override
    public ResponseEntity<Void> createOrder(CreateOrderRequest openApiRequest) {
        final CreateOrderInput useCaseInput = new CreateOrderInput(
                openApiRequest.getExternalOrderId(),
                openApiRequest.getItems().stream()
                        .map(item -> new CreateOrderInput.OrderItemInput(
                                item.getProductCode(),
                                item.getProductName(),
                                item.getQuantity(),
                                item.getUnitPrice()
                        ))
                        .collect(Collectors.toList())
        );

        final CreateOrderOutput output = createOrderUseCase.execute(useCaseInput);

        return ResponseEntity.created(URI.create("/orders/" + output.orderId())).build();
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(UUID id) {
        final GetOrderByIdInput input = new GetOrderByIdInput(id);
        final GetOrderByIdOutput output = getOrderByIdUseCase.execute(input);
        final OrderResponse response = orderMapper.toOrderResponse(output.order());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<OrderListPage> listOrders(String status, String externalOrderId, Integer page, Integer size) {
        final ListOrdersInput input = ListOrdersInput.from(status, externalOrderId, page, size);
        final ListOrdersOutput output = this.listOrdersUseCase.execute(input);

        final OrderListPage response = orderMapper.toOrderListPage(output.pagination());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> cancelOrder(UUID id) {
        final CancelOrderInput input = new CancelOrderInput(id);
        this.cancelOrderUseCase.execute(input);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> retryOrder(UUID id) {
        final RetryOrderInput input = new RetryOrderInput(id);
        this.retryOrderUseCase.execute(input);

        return ResponseEntity.accepted().build();
    }

}