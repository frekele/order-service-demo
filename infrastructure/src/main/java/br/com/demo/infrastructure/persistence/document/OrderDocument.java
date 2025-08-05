package br.com.demo.infrastructure.persistence.document;

import br.com.demo.domain.enums.OrderStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "orders")
public class OrderDocument {
    @Id
    private UUID id;
    private String externalOrderId;
    private BigDecimal totalValue;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemDocument> items;
}