package br.com.demo.domain.model;


import br.com.demo.domain.enums.OrderStatus;
import br.com.demo.domain.valueobject.Money;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order {

    private UUID id;
    private String externalOrderId;
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
    private Money totalValue;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Order create(final String externalOrderId, final List<OrderItem> items) {
        Order order = new Order();
        order.id = UUID.randomUUID();
        order.externalOrderId = externalOrderId;
        order.items = new ArrayList<>(items);
        order.status = OrderStatus.RECEIVED;
        order.createdAt = LocalDateTime.now();
        order.touch();

        return order;
    }

    private void calculateTotalValue() {
        this.totalValue = this.items.stream()
                .map(item -> item.getUnitPrice().multiply(item.getQuantity()))
                .reduce(Money.zero(), Money::add);
    }

    public void startProcessing() {
        if (this.status != OrderStatus.RECEIVED) {
            throw new IllegalStateException("Order must be in RECEIVED state to start processing.");
        }
        this.calculateTotalValue();
        this.status = OrderStatus.PROCESSING;
        this.touch();
    }

    public void complete() {
        if (this.status != OrderStatus.PROCESSING) {
            throw new IllegalStateException("Order must be in PROCESSING state to be completed.");
        }
        this.status = OrderStatus.COMPLETED;
        this.touch();
    }

    public void fail() {
        this.status = OrderStatus.FAILED;
        this.touch();
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

}