package br.com.demo.application.usecase.get;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.domain.exception.NotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultGetOrderByIdUseCase extends GetOrderByIdUseCase {

    private final OrderGateway orderGateway;

    @Override
    public GetOrderByIdOutput execute(GetOrderByIdInput input) {
        final var orderId = input.id();
        return this.orderGateway.findById(orderId)
                .map(GetOrderByIdOutput::from)
                // Use a exceção específica
                .orElseThrow(() -> new NotFoundException("Order with ID %s was not found".formatted(orderId)));
    }
}