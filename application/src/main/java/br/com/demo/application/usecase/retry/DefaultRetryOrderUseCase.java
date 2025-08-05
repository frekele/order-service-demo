package br.com.demo.application.usecase.retry;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.domain.model.Order;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultRetryOrderUseCase extends RetryOrderUseCase {

    private final OrderGateway orderGateway;

    @Override
    public Void execute(RetryOrderInput input) {
        final Order order = this.orderGateway.findById(input.id())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.retry();

        this.orderGateway.save(order);

        return null;
    }
}
