package br.com.demo.application.usecase.get;

import br.com.demo.domain.model.Order;

public record GetOrderByIdOutput(
        Order order
) {
    public static GetOrderByIdOutput from(Order order) {
        return new GetOrderByIdOutput(order);
    }
}
