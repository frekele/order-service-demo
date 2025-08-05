package br.com.demo.infrastructure.gateway;

import br.com.demo.application.core.Pagination;
import br.com.demo.application.core.SearchQuery;
import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.domain.enums.OrderStatus;
import br.com.demo.domain.model.Order;
import br.com.demo.infrastructure.mapper.OrderMapper;
import br.com.demo.infrastructure.persistence.document.OrderDocument;
import br.com.demo.infrastructure.persistence.repository.OrderMongoRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class OrderMongoGateway implements OrderGateway {

    private final OrderMongoRepository mongoRepository;
    private final OrderMapper mapper;

    @Override
    @Cacheable(value = "orders", key = "#id")
    public Optional<Order> findById(UUID id) {
        return mongoRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Cacheable(value = "ordersByExternalId", key = "#externalOrderId")
    public Optional<Order> findByExternalOrderId(String externalOrderId) {
        return mongoRepository.findByExternalOrderId(externalOrderId).map(mapper::toDomain);
    }

    @Override
    @Caching(put = {
            @CachePut(value = "orders", key = "#order.id"),
            @CachePut(value = "ordersByExternalId", key = "#order.externalOrderId")
    })
    public Order save(Order order) {
        final OrderDocument document = mapper.toDocument(order);
        final OrderDocument savedDocument = mongoRepository.save(document);
        return mapper.toDomain(savedDocument);
    }

    @Override
    public Pagination<Order> findAll(SearchQuery query) {
        final Pageable pageable = PageRequest.of(query.page(), query.perPage());

        final OrderDocument probe = new OrderDocument();
        if (query.status() != null && !query.status().isBlank()) {
            probe.setStatus(OrderStatus.valueOf(query.status()));
        }
        if (query.externalOrderId() != null && !query.externalOrderId().isBlank()) {
            probe.setExternalOrderId(query.externalOrderId());
        }

        final Example<OrderDocument> example = Example.of(probe,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
        );

        final Page<OrderDocument> pageResult = this.mongoRepository.findAll(example, pageable);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getContent().stream()
                        .map(mapper::toDomain)
                        .collect(Collectors.toList())
        );
    }
}