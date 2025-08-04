package br.com.demo.infrastructure.api;

import br.com.demo.application.usecase.create.CreateOrderInput;
import br.com.demo.application.usecase.create.CreateOrderUseCase;
import br.com.demo.application.usecase.get.GetOrderByIdInput;
import br.com.demo.application.usecase.get.GetOrderByIdUseCase;
import br.com.demo.application.usecase.list.ListOrdersInput;
import br.com.demo.application.usecase.list.ListOrdersUseCase;
import br.com.demo.infrastructure.mapper.OrderMapper;
import br.com.demo.infrastructure.openapi.order.api.OrdersApiDelegate;
import br.com.demo.infrastructure.openapi.order.model.CreateOrderRequest;
import br.com.demo.infrastructure.openapi.order.model.OrderResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class OrderDelegateImpl implements OrdersApiDelegate {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final ListOrdersUseCase listOrdersUseCase;
    private final OrderMapper orderMapper;

    @Override
    public ResponseEntity<Void> createOrder(CreateOrderRequest openApiRequest) {
        var useCaseInput = new CreateOrderInput(
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

        final var output = createOrderUseCase.execute(useCaseInput);

        return ResponseEntity.created(URI.create("/orders/" + output.orderId())).build();
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(UUID id) {
        final var input = new GetOrderByIdInput(id);
        final var output = getOrderByIdUseCase.execute(input);
        final var response = orderMapper.toOrderResponse(output.order());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<OrderResponse>> listOrders(String status, String externalOrderId, Integer page, Integer size) {
        final var anIn = ListOrdersInput.from(status, externalOrderId, page, size);
        final var output = this.listOrdersUseCase.execute(anIn);

        final var response = output.orders().stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}