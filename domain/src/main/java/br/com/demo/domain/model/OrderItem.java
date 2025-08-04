package br.com.demo.domain.model;

import br.com.demo.domain.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private String productCode;
    private String productName;
    private Integer quantity;
    private Money unitPrice;

}