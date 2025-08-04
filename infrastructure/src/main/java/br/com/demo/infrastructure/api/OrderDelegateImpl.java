package br.com.demo.infrastructure.api;

import br.com.demo.application.usecase.create.CreateOrderInput;
import br.com.demo.application.usecase.create.CreateOrderUseCase;
import br.com.demo.application.usecase.get.GetOrderByIdInput;
import br.com.demo.application.usecase.get.GetOrderByIdUseCase;
import br.com.demo.infrastructure.mapper.OrderMapper;
import br.com.demo.infrastructure.openapi.order.api.OrdersApiDelegate;
import br.com.demo.infrastructure.openapi.order.model.CreateOrderRequest;
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
}