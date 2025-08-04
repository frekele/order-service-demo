package br.com.demo.application.usecase.list;

import br.com.demo.application.core.SearchQuery;
import br.com.demo.application.gateway.OrderGateway;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultListOrdersUseCase extends ListOrdersUseCase {

    private final OrderGateway orderGateway;

    @Override
    public ListOrdersOutput execute(ListOrdersInput anIn) {
        final var aQuery = new SearchQuery(anIn.page(), anIn.size(), anIn.status(), anIn.externalOrderId());
        final var paginationResult = this.orderGateway.findAll(aQuery);
        return ListOrdersOutput.from(paginationResult.items());
    }
}
