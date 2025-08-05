package br.com.demo.application.gateway;

import br.com.demo.domain.model.Order;

public interface NotificationGateway {

    void notifyOrderCompleted(Order order);
}
