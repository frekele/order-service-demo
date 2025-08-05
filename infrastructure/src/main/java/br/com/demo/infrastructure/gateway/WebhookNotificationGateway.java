package br.com.demo.infrastructure.gateway;

import br.com.demo.application.gateway.NotificationGateway;
import br.com.demo.domain.model.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
@Component
public class WebhookNotificationGateway implements NotificationGateway {

    private final RestTemplate restTemplate;

    @Value("${external.product-b.webhook-url}")
    private final String webhookUrl;

    @Override
    public void notifyOrderCompleted(Order order) {
        try {
            log.info("Notifying Product B about completed order {}. URL: {}", order.getId(), webhookUrl);
            restTemplate.postForEntity(webhookUrl, order, String.class);
            log.info("Successfully notified Product B for order {}", order.getId());
        } catch (Exception e) {
            log.error("Failed to notify Product B for order {}: {}", order.getId(), e.getMessage());
        }
    }
}
