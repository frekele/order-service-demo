package br.com.demo.application.usecase.list;

import br.com.demo.application.gateway.OrderGateway;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultListOrdersUseCase extends ListOrdersUseCase {

    private final OrderGateway orderGateway;

    @Override
    public ListOrdersOutput execute(ListOrdersInput anIn) {
        //TODO
        throw new UnsupportedOperationException("ListOrdersUseCase not implemented yet");
    }
}
