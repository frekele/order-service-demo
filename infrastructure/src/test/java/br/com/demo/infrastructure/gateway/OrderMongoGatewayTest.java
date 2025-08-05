package br.com.demo.infrastructure.gateway;

import br.com.demo.application.core.Pagination;
import br.com.demo.application.core.SearchQuery;
import br.com.demo.domain.enums.OrderStatus;
import br.com.demo.domain.model.Order;
import br.com.demo.infrastructure.mapper.OrderMapper;
import br.com.demo.infrastructure.persistence.document.OrderDocument;
import br.com.demo.infrastructure.persistence.repository.OrderMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderMongoGatewayTest {

    @Mock
    private OrderMongoRepository mongoRepository;

    @Mock
    private OrderMapper mapper;

    @InjectMocks
    private OrderMongoGateway orderMongoGateway;

    @Captor
    private ArgumentCaptor<Example<OrderDocument>> exampleCaptor;

    private Order order;
    private OrderDocument orderDocument;
    private UUID orderId;
    private String externalOrderId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        externalOrderId = "ext-123";
        order = Order.builder().id(orderId).externalOrderId(externalOrderId).build();
        orderDocument = new OrderDocument();
        orderDocument.setId(orderId);
        orderDocument.setExternalOrderId(externalOrderId);
    }

    @Nested
    @DisplayName("findById tests")
    class FindByIdTests {

        @Test
        @DisplayName("Should return an order when found by id")
        void shouldReturnOrder_whenFoundById() {
            when(mongoRepository.findById(orderId)).thenReturn(Optional.of(orderDocument));
            when(mapper.toDomain(orderDocument)).thenReturn(order);

            Optional<Order> result = orderMongoGateway.findById(orderId);

            assertThat(result).isPresent().contains(order);
            verify(mongoRepository).findById(orderId);
            verify(mapper).toDomain(orderDocument);
        }

        @Test
        @DisplayName("Should return empty optional when order not found by id")
        void shouldReturnEmpty_whenNotFoundById() {
            when(mongoRepository.findById(orderId)).thenReturn(Optional.empty());

            Optional<Order> result = orderMongoGateway.findById(orderId);

            assertThat(result).isEmpty();
            verify(mongoRepository).findById(orderId);
            verify(mapper, never()).toDomain(any(OrderDocument.class));
        }
    }

    @Nested
    @DisplayName("findByExternalOrderId tests")
    class FindByExternalOrderIdTests {

        @Test
        @DisplayName("Should return an order when found by external order id")
        void shouldReturnOrder_whenFoundByExternalOrderId() {
            when(mongoRepository.findByExternalOrderId(externalOrderId)).thenReturn(Optional.of(orderDocument));
            when(mapper.toDomain(orderDocument)).thenReturn(order);

            Optional<Order> result = orderMongoGateway.findByExternalOrderId(externalOrderId);

            assertThat(result).isPresent().contains(order);
            verify(mongoRepository).findByExternalOrderId(externalOrderId);
            verify(mapper).toDomain(orderDocument);
        }

        @Test
        @DisplayName("Should return empty optional when order not found by external order id")
        void shouldReturnEmpty_whenNotFoundByExternalOrderId() {
            when(mongoRepository.findByExternalOrderId(externalOrderId)).thenReturn(Optional.empty());

            Optional<Order> result = orderMongoGateway.findByExternalOrderId(externalOrderId);

            assertThat(result).isEmpty();
            verify(mongoRepository).findByExternalOrderId(externalOrderId);
            verify(mapper, never()).toDomain(any(OrderDocument.class));
        }
    }

    @Nested
    @DisplayName("save tests")
    class SaveTests {

        @Test
        @DisplayName("Should save and return an order")
        void shouldSaveAndReturnOrder() {
            when(mapper.toDocument(order)).thenReturn(orderDocument);
            when(mongoRepository.save(orderDocument)).thenReturn(orderDocument);
            when(mapper.toDomain(orderDocument)).thenReturn(order);

            Order savedOrder = orderMongoGateway.save(order);

            assertThat(savedOrder).isEqualTo(order);
            verify(mapper).toDocument(order);
            verify(mongoRepository).save(orderDocument);
            verify(mapper).toDomain(orderDocument);
        }
    }

    @Nested
    @DisplayName("findAll tests")
    class FindAllTests {

        @Test
        @DisplayName("Should return a pagination of orders with all filters")
        void shouldReturnPaginationOfOrders_withAllFilters() {
            SearchQuery query = new SearchQuery(0, 10, "RECEIVED", externalOrderId);
            Pageable pageable = PageRequest.of(0, 10);
            List<OrderDocument> documents = Collections.singletonList(orderDocument);
            Page<OrderDocument> pageResult = new PageImpl<>(documents, pageable, 1);

            when(mongoRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(pageResult);
            when(mapper.toDomain(orderDocument)).thenReturn(order);

            Pagination<Order> result = orderMongoGateway.findAll(query);

            assertThat(result.currentPage()).isZero();
            assertThat(result.perPage()).isEqualTo(10);
            assertThat(result.total()).isEqualTo(1);
            assertThat(result.items()).hasSize(1).contains(order);

            verify(mongoRepository).findAll(exampleCaptor.capture(), any(Pageable.class));
            Example<OrderDocument> capturedExample = exampleCaptor.getValue();
            assertThat(capturedExample.getProbe().getStatus()).isEqualTo(OrderStatus.RECEIVED);
            assertThat(capturedExample.getProbe().getExternalOrderId()).isEqualTo(externalOrderId);
        }

        @Test
        @DisplayName("Should return a pagination of orders with only status filter")
        void shouldReturnPaginationOfOrders_withOnlyStatusFilter() {
            SearchQuery query = new SearchQuery(0, 10, "PROCESSING", null);
            Pageable pageable = PageRequest.of(0, 10);
            List<OrderDocument> documents = Collections.singletonList(orderDocument);
            Page<OrderDocument> pageResult = new PageImpl<>(documents, pageable, 1);

            when(mongoRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(pageResult);

            orderMongoGateway.findAll(query);

            verify(mongoRepository).findAll(exampleCaptor.capture(), any(Pageable.class));
            Example<OrderDocument> capturedExample = exampleCaptor.getValue();
            assertThat(capturedExample.getProbe().getStatus()).isEqualTo(OrderStatus.PROCESSING);
            assertThat(capturedExample.getProbe().getExternalOrderId()).isNull();
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for invalid status")
        void shouldThrowException_forInvalidStatus() {
            SearchQuery query = new SearchQuery(0, 10, "INVALID_STATUS", null);

            assertThrows(IllegalArgumentException.class, () -> orderMongoGateway.findAll(query));
            verify(mongoRepository, never()).findAll(any(Example.class), any(Pageable.class));
        }
    }
}