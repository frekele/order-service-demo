package br.com.demo.infrastructure.gateway;

import br.com.demo.application.gateway.OrderEventGateway;
import br.com.demo.domain.model.Order;
import br.com.demo.infrastructure.config.RabbitMQConfig;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class OrderEventRabbitMQGatewayImpl implements OrderEventGateway {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendOrderCreated(Order order) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                order
        );
    }
}
