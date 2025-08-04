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
    private LocalDateTime receivedAt;

}