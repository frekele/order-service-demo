package br.com.demo.application.usecase.list;

import br.com.demo.application.core.SearchQuery;
import br.com.demo.application.gateway.OrderGateway;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultListOrdersUseCase extends ListOrdersUseCase {

    private final OrderGateway orderGateway;

    @Override
    public ListOrdersOutput execute(ListOrdersInput input) {
        final var aQuery = new SearchQuery(input.page(), input.size(), input.status(), input.externalOrderId());
        final var paginationResult = this.orderGateway.findAll(aQuery);
        return ListOrdersOutput.from(paginationResult);
    }
}
