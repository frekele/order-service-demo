package br.com.demo.infrastructure.persistence.document;

import br.com.demo.domain.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderDocumentTest {

    @Test
    void shouldSetAndGetPropertiesCorrectly() {
        OrderDocument orderDocument = new OrderDocument();
        UUID id = UUID.randomUUID();
        String externalOrderId = "ext-123";
        BigDecimal totalValue = new BigDecimal("199.99");
        OrderStatus status = OrderStatus.RECEIVED;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();
        OrderItemDocument item = new OrderItemDocument();
        item.setProductCode("prod-abc");
        item.setProductName("prod-abc-name");
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("99.995"));
        List<OrderItemDocument> items = Collections.singletonList(item);

        orderDocument.setId(id);
        orderDocument.setExternalOrderId(externalOrderId);
        orderDocument.setTotalValue(totalValue);
        orderDocument.setStatus(status);
        orderDocument.setCreatedAt(createdAt);
        orderDocument.setUpdatedAt(updatedAt);
        orderDocument.setItems(items);

        assertAll("Ensure all properties are set and retrieved correctly",
                () -> assertEquals(id, orderDocument.getId()),
                () -> assertEquals(externalOrderId, orderDocument.getExternalOrderId()),
                () -> assertEquals(totalValue, orderDocument.getTotalValue()),
                () -> assertEquals(status, orderDocument.getStatus()),
                () -> assertEquals(createdAt, orderDocument.getCreatedAt()),
                () -> assertEquals(updatedAt, orderDocument.getUpdatedAt()),
                () -> assertEquals(items, orderDocument.getItems())
        );
    }

    @Test
    void shouldCorrectlyEvaluateEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        String externalOrderId = "ext-456";
        BigDecimal totalValue = new BigDecimal("150.00");
        OrderStatus status = OrderStatus.PROCESSING;
        LocalDateTime now = LocalDateTime.now();
        List<OrderItemDocument> items = Collections.emptyList();

        OrderDocument order1 = createOrderDocument(id, externalOrderId, totalValue, status, now, now, items);
        OrderDocument order2 = createOrderDocument(id, externalOrderId, totalValue, status, now, now, items);
        OrderDocument order3 = createOrderDocument(UUID.randomUUID(), "ext-789", totalValue, status, now, now, items);
        OrderDocument order4 = createOrderDocument(id, externalOrderId, new BigDecimal("99.99"), status, now, now, items);

        assertEquals(order1, order1, "An object must be equal to itself.");

        assertEquals(order1, order2, "Symmetric property: if o1.equals(o2), then o2.equals(o1).");
        assertEquals(order2, order1, "Symmetric property: if o1.equals(o2), then o2.equals(o1).");

        assertEquals(order1.hashCode(), order2.hashCode(), "Equal objects must have equal hash codes.");

        assertNotEquals(order1, order3, "Objects with different IDs should not be equal.");
        assertNotEquals(order1, order4, "Objects with different total values should not be equal.");

        assertNotEquals(null, order1, "An object should not be equal to null.");
        assertNotEquals(order1, new Object(), "An object should not be equal to an object of a different type.");
    }

    @Test
    void shouldProduceAValidStringRepresentation() {
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        OrderDocument orderDocument = createOrderDocument(id, "ext-toString", BigDecimal.TEN, OrderStatus.COMPLETED, LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList());

        String toStringResult = orderDocument.toString();

        assertNotNull(toStringResult);
        assertTrue(toStringResult.startsWith("OrderDocument("), "toString should start with the class name.");
        assertTrue(toStringResult.contains("id=123e4567-e89b-12d3-a456-426614174000"), "toString should contain the id.");
        assertTrue(toStringResult.contains("externalOrderId=ext-toString"), "toString should contain the externalOrderId.");
        assertTrue(toStringResult.contains("status=COMPLETED"), "toString should contain the status.");
        assertTrue(toStringResult.endsWith(")"), "toString should end with a parenthesis.");
    }

    private OrderDocument createOrderDocument(UUID id, String externalOrderId, BigDecimal totalValue, OrderStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, List<OrderItemDocument> items) {
        OrderDocument orderDocument = new OrderDocument();
        orderDocument.setId(id);
        orderDocument.setExternalOrderId(externalOrderId);
        orderDocument.setTotalValue(totalValue);
        orderDocument.setStatus(status);
        orderDocument.setCreatedAt(createdAt);
        orderDocument.setUpdatedAt(updatedAt);
        orderDocument.setItems(items);
        return orderDocument;
    }
}