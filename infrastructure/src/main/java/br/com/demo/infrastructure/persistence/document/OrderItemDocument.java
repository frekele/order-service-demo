package br.com.demo.infrastructure.persistence.document;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDocument {
    private String productCode;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
}
