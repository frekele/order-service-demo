package br.com.demo.application.usecase.cancel;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.domain.exception.NotFoundException;
import br.com.demo.domain.model.Order;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultCancelOrderUseCase extends CancelOrderUseCase {

    private final OrderGateway orderGateway;

    @Override
    public Void execute(CancelOrderInput input) {
        final Order order = this.orderGateway.findById(input.id())
                .orElseThrow(() -> new NotFoundException("Order with ID %s was not found".formatted(input.id())));

        order.cancel();
        this.orderGateway.save(order);

        return null;
    }
}