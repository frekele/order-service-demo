package br.com.demo.infrastructure.mapper;

import br.com.demo.application.core.Pagination;
import br.com.demo.domain.enums.OrderStatus;
import br.com.demo.domain.model.Order;
import br.com.demo.domain.model.OrderItem;
import br.com.demo.domain.valueobject.Money;
import br.com.demo.infrastructure.openapi.order.model.OrderItemResponse;
import br.com.demo.infrastructure.openapi.order.model.OrderResponse;
import br.com.demo.infrastructure.persistence.document.OrderDocument;
import br.com.demo.infrastructure.persistence.document.OrderItemDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {OrderMapperImpl.class, MoneyMapper.class, OffsetDateTimeMapper.class})
class OrderMapperTest {

    @Autowired
    private OrderMapper mapper;

    @Test
    @DisplayName("Should correctly map from Order domain object to OrderDocument")
    void shouldMapOrderToOrderDocument() {
        final var orderItem = OrderItem.builder()
                .productCode("P1")
                .productName("Product 1")
                .quantity(2)
                .unitPrice(Money.of(new BigDecimal("10.50")))
                .build();

        final var order = Order.create("EXT-123", List.of(orderItem));
        order.startProcessing();
        order.calculateTotalValue();
        order.complete();

        final var document = mapper.toDocument(order);

        assertNotNull(document);
        assertEquals(order.getId(), document.getId());
        assertEquals(order.getExternalOrderId(), document.getExternalOrderId());
        assertEquals(order.getStatus(), document.getStatus());
        assertEquals(order.getCreatedAt(), document.getCreatedAt());
        assertEquals(order.getUpdatedAt(), document.getUpdatedAt());
        assertEquals(order.getTotalValue().getAmount(), document.getTotalValue());

        assertNotNull(document.getItems());
        assertEquals(1, document.getItems().size());
        final var documentItem = document.getItems().getFirst();
        assertEquals(orderItem.getProductCode(), documentItem.getProductCode());
        assertEquals(orderItem.getQuantity(), documentItem.getQuantity());
        assertEquals(orderItem.getUnitPrice().getAmount(), documentItem.getUnitPrice());
    }

    @Test
    @DisplayName("Should correctly map from OrderDocument to Order domain object")
    void shouldMapOrderDocumentToOrder() {
        final var document = new OrderDocument();
        document.setId(UUID.randomUUID());
        document.setExternalOrderId("EXT-456");
        document.setStatus(OrderStatus.COMPLETED);
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now().plusHours(1));
        document.setTotalValue(new BigDecimal("21.00"));

        final var order = mapper.toDomain(document);

        assertNotNull(order);
        assertEquals(document.getId(), order.getId());
        assertEquals(document.getExternalOrderId(), order.getExternalOrderId());
        assertEquals(document.getStatus(), order.getStatus());
        assertEquals(document.getCreatedAt(), order.getCreatedAt());
        assertEquals(document.getUpdatedAt(), order.getUpdatedAt());
        assertEquals(document.getTotalValue(), order.getTotalValue().getAmount());
    }

    @Test
    @DisplayName("Should correctly map from Application Pagination to API OrderListPage")
    void shouldMapPaginationToOrderListPage() {
        final var order = Order.create("EXT-789", List.of());
        final var pagination = new Pagination<>(0, 10, 1L, List.of(order));

        final var listPage = mapper.toOrderListPage(pagination);

        assertNotNull(listPage);
        assertEquals(pagination.currentPage(), listPage.getCurrentPage());
        assertEquals(pagination.perPage(), listPage.getPerPage());
        assertEquals(pagination.total(), listPage.getTotal());
        assertEquals(1, listPage.getItems().size());
        assertEquals(order.getId(), listPage.getItems().getFirst().getId());
    }

    @Test
    @DisplayName("Should correctly map from OrderItem domain object to OrderItemDocument")
    void shouldMapOrderItemToOrderItemDocument() {
        final var orderItem = OrderItem.builder()
                .productCode("P1")
                .productName("Product 1")
                .quantity(2)
                .unitPrice(Money.of(new BigDecimal("10.50")))
                .build();

        final var documentItem = mapper.toOrderItemDocument(orderItem);

        assertNotNull(documentItem);
        assertEquals(orderItem.getProductCode(), documentItem.getProductCode());
        assertEquals(orderItem.getProductName(), documentItem.getProductName());
        assertEquals(orderItem.getQuantity(), documentItem.getQuantity());
        assertEquals(orderItem.getUnitPrice().getAmount(), documentItem.getUnitPrice());
    }

    @Test
    @DisplayName("Should correctly map from OrderItemDocument to OrderItem domain object")
    void shouldMapOrderItemDocumentToOrderItem() {
        final var documentItem = new OrderItemDocument();
        documentItem.setProductCode("P2");
        documentItem.setProductName("Product 2");
        documentItem.setQuantity(5);
        documentItem.setUnitPrice(new BigDecimal("5.25"));

        final var orderItem = mapper.toOrderItemDomain(documentItem);

        assertNotNull(orderItem);
        assertEquals(documentItem.getProductCode(), orderItem.getProductCode());
        assertEquals(documentItem.getProductName(), orderItem.getProductName());
        assertEquals(documentItem.getQuantity(), orderItem.getQuantity());
        assertEquals(documentItem.getUnitPrice(), orderItem.getUnitPrice().getAmount());
    }

    @Test
    @DisplayName("Should correctly map from Order domain object to OrderResponse")
    void shouldMapOrderToOrderResponse() {
        final var orderItem = OrderItem.builder()
                .productCode("P1")
                .productName("Product 1")
                .quantity(1)
                .unitPrice(Money.of(new BigDecimal("100.00")))
                .build();
        final var order = Order.create("EXT-API-1", List.of(orderItem));
        order.calculateTotalValue();

        final OrderResponse response = mapper.toOrderResponse(order);

        assertNotNull(response);
        assertEquals(order.getId(), response.getId());
        assertEquals(order.getExternalOrderId(), response.getExternalOrderId());
        assertNotNull(response.getStatus());
        assertEquals(order.getStatus().name(), response.getStatus().name());
        assertNotNull(response.getCreatedAt());
        assertEquals(order.getCreatedAt(), response.getCreatedAt().toLocalDateTime());
        assertNotNull(response.getUpdatedAt());
        assertEquals(order.getUpdatedAt(), response.getUpdatedAt().toLocalDateTime());
        assertEquals(order.getTotalValue().getAmount(), response.getTotalValue());
        assertEquals(1, response.getItems().size());
    }

    @Test
    @DisplayName("Should correctly map from OrderItem domain object to OrderItemResponse")
    void shouldMapOrderItemToOrderItemResponse() {
        final var orderItem = OrderItem.builder()
                .productCode("P-API-1")
                .productName("Product API 1")
                .quantity(3)
                .unitPrice(Money.of(new BigDecimal("12.00")))
                .build();

        final OrderItemResponse responseItem = mapper.toOrderItemResponse(orderItem);

        assertNotNull(responseItem);
        assertEquals(orderItem.getProductCode(), responseItem.getProductCode());
        assertEquals(orderItem.getProductName(), responseItem.getProductName());
        assertEquals(orderItem.getQuantity(), responseItem.getQuantity());
        assertEquals(orderItem.getUnitPrice().getAmount(), responseItem.getUnitPrice());
    }

    @Test
    @DisplayName("Should handle null inputs gracefully returning null")
    void shouldHandleNulls() {
        assertNull(mapper.toDocument(null));
        assertNull(mapper.toDomain(null));
        assertNull(mapper.toOrderItemDocument(null));
        assertNull(mapper.toOrderItemDomain(null));
        assertNull(mapper.toOrderResponse(null));
        assertNull(mapper.toOrderItemResponse(null));
        assertNull(mapper.toOrderListPage(null));
    }
}