package br.com.demo.application.usecase.list;

import br.com.demo.domain.model.Order;

import java.util.List;

public record ListOrdersOutput(
        List<Order> orders
) {
    public static ListOrdersOutput from(List<Order> orders) {
        return new ListOrdersOutput(orders);
    }
}
