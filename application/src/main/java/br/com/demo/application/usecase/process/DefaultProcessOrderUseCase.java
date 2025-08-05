package br.com.demo.application.usecase.process;

import br.com.demo.application.gateway.NotificationGateway;
import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.domain.model.Order;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultProcessOrderUseCase extends ProcessOrderUseCase {

    private final OrderGateway orderGateway;
    private final NotificationGateway notificationGateway;

    @Override
    public Void execute(ProcessOrderInput input) {
        final Order order = input.order();

        order.startProcessing();
        this.orderGateway.save(order);

        order.calculateTotalValue();
        order.complete();
        this.notificationGateway.notifyOrderCompleted(order);

        this.orderGateway.save(order);

        return null;
    }
}