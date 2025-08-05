package br.com.demo.application.usecase.process;

import br.com.demo.domain.model.Order;

public record ProcessOrderInput(
        Order order
) {
}