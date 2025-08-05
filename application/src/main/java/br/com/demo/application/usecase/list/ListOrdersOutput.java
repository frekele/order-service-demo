package br.com.demo.application.usecase.list;

import br.com.demo.application.core.Pagination;
import br.com.demo.domain.model.Order;

public record ListOrdersOutput(
        Pagination<Order> pagination
) {
    public static ListOrdersOutput from(Pagination<Order> pagination) {
        return new ListOrdersOutput(pagination);
    }
}
