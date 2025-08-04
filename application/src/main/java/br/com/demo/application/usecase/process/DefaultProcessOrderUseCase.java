package br.com.demo.application.usecase.process;

import br.com.demo.application.gateway.OrderGateway;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultProcessOrderUseCase extends ProcessOrderUseCase {

    private final OrderGateway orderGateway;

    @Override
    public Void execute(ProcessOrderInput anIn) {
        final var order = anIn.order();

        order.startProcessing();
        this.orderGateway.save(order);

        order.calculateTotalValue();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        order.complete();
        this.orderGateway.save(order);

        return null;
    }
}