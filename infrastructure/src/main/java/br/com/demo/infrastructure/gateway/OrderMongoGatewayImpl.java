package br.com.demo.infrastructure.gateway;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.domain.model.Order;
import br.com.demo.infrastructure.mapper.OrderMapper;
import br.com.demo.infrastructure.persistence.repository.OrderMongoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Component
public class OrderMongoGatewayImpl implements OrderGateway {

    private final OrderMongoRepository mongoRepository;
    private final OrderMapper mapper;

    @Override
    public Optional<Order> findById(UUID id) {
        return mongoRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Order> findByExternalOrderId(String externalOrderId) {
        return mongoRepository.findByExternalOrderId(externalOrderId).map(mapper::toDomain);
    }

    @Override
    public Order save(Order order) {
        final var document = mapper.toDocument(order);
        final var savedDocument = mongoRepository.save(document);
        return mapper.toDomain(savedDocument);
    }
}