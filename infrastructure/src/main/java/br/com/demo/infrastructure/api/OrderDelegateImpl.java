package br.com.demo.infrastructure.api;

import br.com.demo.application.usecase.create.CreateOrderInput;
import br.com.demo.application.usecase.create.CreateOrderUseCase;
import br.com.demo.infrastructure.openapi.order.api.OrdersApiDelegate;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class OrderDelegateImpl implements OrdersApiDelegate {

    private final CreateOrderUseCase createOrderUseCase;

    @Override
    public ResponseEntity<Void> createOrder(br.com.demo.infrastructure.openapi.order.model.CreateOrderRequest openApiRequest) {
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
}