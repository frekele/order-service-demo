package br.com.demo.application.gateway;


import br.com.demo.application.core.Pagination;
import br.com.demo.application.core.SearchQuery;
import br.com.demo.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderGateway {

    Optional<Order> findById(UUID id);

    Optional<Order> findByExternalOrderId(String externalOrderId);

    Order save(Order order);

    Pagination<Order> findAll(SearchQuery query);
}
