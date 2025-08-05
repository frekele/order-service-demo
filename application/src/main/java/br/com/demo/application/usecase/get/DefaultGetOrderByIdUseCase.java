package br.com.demo.application.usecase.get;

import br.com.demo.application.gateway.OrderGateway;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultGetOrderByIdUseCase extends GetOrderByIdUseCase {

    private final OrderGateway orderGateway;

    @Override
    public GetOrderByIdOutput execute(GetOrderByIdInput input) {
        return this.orderGateway.findById(input.id())
                .map(GetOrderByIdOutput::from)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}