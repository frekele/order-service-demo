package br.com.demo.application.usecase.cancel;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.domain.model.Order;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultCancelOrderUseCase extends CancelOrderUseCase {

    private final OrderGateway orderGateway;

    @Override
    public Void execute(CancelOrderInput anIn) {
        final Order order = this.orderGateway.findById(anIn.id())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.cancel();
        this.orderGateway.save(order);

        return null;
    }
}