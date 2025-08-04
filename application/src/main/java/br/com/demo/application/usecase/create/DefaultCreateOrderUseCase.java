package br.com.demo.application.usecase.create;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.domain.model.Order;
import br.com.demo.domain.model.OrderItem;
import br.com.demo.domain.valueobject.Money;
import lombok.AllArgsConstructor;

import java.util.stream.Collectors;

@AllArgsConstructor
public class DefaultCreateOrderUseCase extends CreateOrderUseCase {

    private final OrderGateway orderGateway;

    @Override
    public CreateOrderOutput execute(CreateOrderInput input) {
        this.orderGateway.findByExternalOrderId(input.externalOrderId()).ifPresent(order -> {
            throw new IllegalArgumentException("Request with external ID " + input.externalOrderId() + " already exists.");
        });

        var orderItems = input.items().stream()
                .map(item -> OrderItem.builder()
                        .productCode(item.productCode())
                        .productName(item.productName())
                        .quantity(item.quantity())
                        .unitPrice(Money.of(item.unitPrice()))
                        .build())
                .collect(Collectors.toList());

        Order newOrder = Order.create(input.externalOrderId(), orderItems);
        newOrder.startProcessing();

        final Order order = this.orderGateway.save(newOrder);
        return CreateOrderOutput.from(order.getId());
    }

}
