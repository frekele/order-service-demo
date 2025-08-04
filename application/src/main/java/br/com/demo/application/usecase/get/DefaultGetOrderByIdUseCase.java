package br.com.demo.application.usecase.get;

import br.com.demo.application.gateway.OrderGateway;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultGetOrderByIdUseCase extends GetOrderByIdUseCase {

    private final OrderGateway orderGateway;

    @Override
    public GetOrderByIdOutput execute(GetOrderByIdInput anIn) {
        return this.orderGateway.findById(anIn.id())
                .map(GetOrderByIdOutput::from)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}