package br.com.demo.application.gateway;

import br.com.demo.domain.model.Order;

public interface OrderEventGateway {

    void sendOrderCreated(Order order);

}
