package br.com.demo.infrastructure.persistence.document;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemDocumentTest {

    @Test
    void testGettersAndSetters() {
        OrderItemDocument orderItem = new OrderItemDocument();
        String productCode = "P123";
        String productName = "Test Product";
        Integer quantity = 10;
        BigDecimal unitPrice = new BigDecimal("19.99");

        orderItem.setProductCode(productCode);
        orderItem.setProductName(productName);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(unitPrice);

        assertEquals(productCode, orderItem.getProductCode());
        assertEquals(productName, orderItem.getProductName());
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(unitPrice, orderItem.getUnitPrice());
    }

    @Test
    void testEqualsAndHashCode() {
        BigDecimal price = new BigDecimal("25.50");

        OrderItemDocument item1 = new OrderItemDocument();
        item1.setProductCode("P456");
        item1.setProductName("Another Product");
        item1.setQuantity(5);
        item1.setUnitPrice(price);

        OrderItemDocument item2 = new OrderItemDocument();
        item2.setProductCode("P456");
        item2.setProductName("Another Product");
        item2.setQuantity(5);
        item2.setUnitPrice(price);

        OrderItemDocument item3 = new OrderItemDocument();
        item3.setProductCode("P789");
        item3.setProductName("Different Product");
        item3.setQuantity(2);
        item3.setUnitPrice(new BigDecimal("10.00"));

        assertEquals(item1, item2, "Two items with the same properties should be equal.");
        assertEquals(item1.hashCode(), item2.hashCode(), "Hash codes should be the same for equal objects.");
        assertNotEquals(item1, item3, "Items with different properties should not be equal.");
        assertEquals(item1, item1, "An item should be equal to itself.");
        assertNotEquals(null, item1, "An item should not be equal to null.");
        assertNotEquals(item1, new Object(), "An item should not be equal to an object of a different type.");
    }

    @Test
    void testToString() {
        OrderItemDocument orderItem = new OrderItemDocument();
        orderItem.setProductCode("P999");
        orderItem.setProductName("String Test Product");
        orderItem.setQuantity(1);
        orderItem.setUnitPrice(new BigDecimal("99.99"));

        String toStringResult = orderItem.toString();

        assertTrue(toStringResult.contains("productCode=P999"), "toString should contain the product code.");
        assertTrue(toStringResult.contains("productName=String Test Product"), "toString should contain the product name.");
        assertTrue(toStringResult.contains("quantity=1"), "toString should contain the quantity.");
        assertTrue(toStringResult.contains("unitPrice=99.99"), "toString should contain the unit price.");
    }
}