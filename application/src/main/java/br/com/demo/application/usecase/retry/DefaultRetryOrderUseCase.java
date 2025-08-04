package br.com.demo.application.usecase.retry;

import br.com.demo.application.gateway.OrderGateway;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultRetryOrderUseCase extends RetryOrderUseCase {

    private final OrderGateway orderGateway;

    @Override
    public Void execute(RetryOrderInput anIn) {
        final var order = this.orderGateway.findById(anIn.id())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.retry();

        this.orderGateway.save(order);

        return null;
    }
}
