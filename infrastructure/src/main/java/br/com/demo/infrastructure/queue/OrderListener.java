package br.com.demo.infrastructure.queue;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.domain.model.Order;
import br.com.demo.infrastructure.config.RabbitMQConfig;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderListener {

    private static final Logger log = LoggerFactory.getLogger(OrderListener.class);
    private final OrderGateway orderGateway;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void onOrderCreated(final Order order) {
        log.info("Received order to process: {}", order.getId());
        try {
            order.startProcessing();
            orderGateway.save(order);
            log.info("Order processed and saved successfully: {}", order.getId());
        } catch (Exception e) {
            log.error("Error processing order {}: {}", order.getId(), e.getMessage());
            order.fail();
            orderGateway.save(order);
        }
    }
}
