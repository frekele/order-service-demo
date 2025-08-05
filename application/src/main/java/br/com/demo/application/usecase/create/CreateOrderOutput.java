package br.com.demo.application.usecase.create;

import java.util.UUID;

public record CreateOrderOutput(
        UUID orderId
) {

    public static CreateOrderOutput from(UUID orderId) {
        return new CreateOrderOutput(orderId);
    }
}