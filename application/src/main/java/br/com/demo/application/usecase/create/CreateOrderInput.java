package br.com.demo.application.usecase.create;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderInput(
        String externalOrderId,
        List<OrderItemInput> items
) {
    public record OrderItemInput(
            String productCode,
            String productName,
            Integer quantity,
            BigDecimal unitPrice
    ) {}
}