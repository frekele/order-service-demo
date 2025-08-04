package br.com.demo.domain.model;


import br.com.demo.domain.enums.OrderStatus;
import br.com.demo.domain.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private UUID id;
    private String externalOrderId;
    private List<OrderItem> items = new ArrayList<>();
    private Money totalValue;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void calculateTotal() {
        Money total = Money.zero();
        if (items != null) {
            for (OrderItem item : items) {
                total = total.add(item.getUnitPrice().multiply(item.getQuantity()));
            }
        }
        this.totalValue = total;
    }

    public void markAsReceived() {
        this.status = OrderStatus.RECEIVED;
        this.touch();
    }

    public void markAsProcessing() {
        this.status = OrderStatus.PROCESSING;
        this.touch();
    }

    public void markAsCompleted() {
        this.status = OrderStatus.COMPLETED;
        this.touch();
    }

    public void markAsFailed() {
        this.status = OrderStatus.FAILED;
        this.touch();
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

}