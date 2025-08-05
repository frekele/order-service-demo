package br.com.demo.application.usecase.create;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CreateOrderInputTest {

    @Test
    @DisplayName("Should correctly create CreateOrderInput with a list of items")
    void shouldCreateOrderInputWithValidData() {
        var orderItemInput1 = new CreateOrderInput.OrderItemInput(
                "P001",
                "Product 1",
                2,
                new BigDecimal("10.50")
        );
        var orderItemInput2 = new CreateOrderInput.OrderItemInput(
                "P002",
                "Product 2",
                1,
                new BigDecimal("25.00")
        );
        var items = List.of(orderItemInput1, orderItemInput2);
        var externalOrderId = "ext-12345";

        var createOrderInput = new CreateOrderInput(externalOrderId, items);

        assertThat(createOrderInput).isNotNull();
        assertThat(createOrderInput.externalOrderId()).isEqualTo(externalOrderId);
        assertThat(createOrderInput.items()).hasSize(2);
        assertThat(createOrderInput.items()).containsExactly(orderItemInput1, orderItemInput2);
    }

    @Test
    @DisplayName("Should correctly create CreateOrderInput with an empty list of items")
    void shouldCreateOrderInputWithEmptyItemsList() {
        var items = Collections.<CreateOrderInput.OrderItemInput>emptyList();
        var externalOrderId = "ext-67890";

        var createOrderInput = new CreateOrderInput(externalOrderId, items);

        assertThat(createOrderInput).isNotNull();
        assertThat(createOrderInput.externalOrderId()).isEqualTo(externalOrderId);
        assertThat(createOrderInput.items()).isNotNull();
        assertThat(createOrderInput.items()).isEmpty();
    }

    @Test
    @DisplayName("Should correctly create OrderItemInput and retrieve its properties")
    void shouldCreateOrderItemInputWithValidData() {
        var productCode = "SKU-ABC";
        var productName = "Test Product";
        var quantity = 5;
        var unitPrice = new BigDecimal("99.99");

        var orderItemInput = new CreateOrderInput.OrderItemInput(
                productCode,
                productName,
                quantity,
                unitPrice
        );

        assertThat(orderItemInput).isNotNull();
        assertThat(orderItemInput.productCode()).isEqualTo(productCode);
        assertThat(orderItemInput.productName()).isEqualTo(productName);
        assertThat(orderItemInput.quantity()).isEqualTo(quantity);
        assertThat(orderItemInput.unitPrice()).isEqualTo(unitPrice);
    }

    @Test
    @DisplayName("Should allow null values for fields in CreateOrderInput and OrderItemInput")
    void shouldAllowNullValuesForFields() {
        var orderItemWithNulls = new CreateOrderInput.OrderItemInput(null, null, null, null);
        var items = List.of(orderItemWithNulls);

        var createOrderInputWithNulls = new CreateOrderInput(null, items);

        assertThat(createOrderInputWithNulls.externalOrderId()).isNull();
        assertThat(createOrderInputWithNulls.items()).hasSize(1);

        var retrievedItem = createOrderInputWithNulls.items().getFirst();
        assertThat(retrievedItem.productCode()).isNull();
        assertThat(retrievedItem.productName()).isNull();
        assertThat(retrievedItem.quantity()).isNull();
        assertThat(retrievedItem.unitPrice()).isNull();
    }
}