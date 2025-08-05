package br.com.demo.domain.model;

import br.com.demo.domain.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    @DisplayName("Should create OrderItem using builder and verify getters")
    void shouldCreateOrderItemUsingBuilderAndGettersShouldWork() {
        String productCode = "P123";
        String productName = "Test Product";
        Integer quantity = 10;
        Money unitPrice = Money.of(new BigDecimal("19.99"));

        OrderItem orderItem = OrderItem.builder()
                .productCode(productCode)
                .productName(productName)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .build();

        assertNotNull(orderItem);
        assertEquals(productCode, orderItem.getProductCode());
        assertEquals(productName, orderItem.getProductName());
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(unitPrice, orderItem.getUnitPrice());
    }

    @Test
    @DisplayName("Should be equal for two instances with the same values")
    void shouldBeEqualForSameValues() {
        OrderItem item1 = OrderItem.builder()
                .productCode("P123")
                .productName("Test Product")
                .quantity(10)
                .unitPrice(Money.of(new BigDecimal("19.99")))
                .build();

        OrderItem item2 = OrderItem.builder()
                .productCode("P123")
                .productName("Test Product")
                .quantity(10)
                .unitPrice(Money.of(new BigDecimal("19.99")))
                .build();

        assertEquals(item1, item2);
    }

    @Test
    @DisplayName("Should not be equal for two instances with different values")
    void shouldNotBeEqualForDifferentValues() {
        OrderItem item1 = OrderItem.builder()
                .productCode("P123")
                .productName("Test Product")
                .quantity(10)
                .unitPrice(Money.of(new BigDecimal("19.99")))
                .build();

        OrderItem item2 = OrderItem.builder()
                .productCode("P456") // Different product code
                .productName("Test Product")
                .quantity(10)
                .unitPrice(Money.of(new BigDecimal("19.99")))
                .build();

        assertNotEquals(item1, item2);
    }

    @Test
    @DisplayName("Should not be equal to null or an object of a different type")
    void shouldNotBeEqualToNullOrDifferentType() {
        OrderItem item1 = OrderItem.builder()
                .productCode("P123")
                .productName("Test Product")
                .quantity(10)
                .unitPrice(Money.of(new BigDecimal("19.99")))
                .build();

        assertNotEquals(null, item1);
        assertNotEquals(item1, new Object());
    }

    @Test
    @DisplayName("Should have the same hash code for two equal objects")
    void shouldHaveSameHashCodeForEqualObjects() {
        OrderItem item1 = OrderItem.builder()
                .productCode("P123")
                .productName("Test Product")
                .quantity(10)
                .unitPrice(Money.of(new BigDecimal("19.99")))
                .build();

        OrderItem item2 = OrderItem.builder()
                .productCode("P123")
                .productName("Test Product")
                .quantity(10)
                .unitPrice(Money.of(new BigDecimal("19.99")))
                .build();

        assertEquals(item1.hashCode(), item2.hashCode());
    }
}