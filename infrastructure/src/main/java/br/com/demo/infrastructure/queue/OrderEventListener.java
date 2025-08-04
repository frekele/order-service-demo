package br.com.demo.infrastructure.queue;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.application.usecase.process.ProcessOrderInput;
import br.com.demo.application.usecase.process.ProcessOrderUseCase;
import br.com.demo.domain.model.Order;
import br.com.demo.infrastructure.config.RabbitMQConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class OrderEventListener {

    private final ProcessOrderUseCase processOrderUseCase;
    private final OrderGateway orderGateway;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void onOrderCreated(final Order order) {
        log.info("Received order to process: {}", order.getId());
        try {
            final var input = new ProcessOrderInput(order);
            this.processOrderUseCase.execute(input);
            log.info("Order processed and saved successfully: {}", order.getId());
        } catch (Exception e) {
            log.error("A critical error occurred while processing order {}: {}", order.getId(), e.getMessage());
            order.fail();
            this.orderGateway.save(order);
        }
    }
}
