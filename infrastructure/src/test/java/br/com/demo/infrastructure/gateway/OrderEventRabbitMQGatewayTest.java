package br.com.demo.infrastructure.gateway;

import br.com.demo.domain.model.Order;
import br.com.demo.infrastructure.config.RabbitMQConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderEventRabbitMQGatewayTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderEventRabbitMQGateway orderEventRabbitMQGateway;

    @Test
    @DisplayName("Should send order created event to RabbitMQ")
    void shouldSendOrderCreatedEvent() {
        final Order order = Order.builder()
                .id(UUID.randomUUID())
                .build();

        orderEventRabbitMQGateway.sendOrderCreated(order);

        verify(rabbitTemplate, times(1)).convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, order);
    }
}