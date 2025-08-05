package br.com.demo.infrastructure.gateway;

import br.com.demo.domain.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebhookNotificationGatewayTest {

    @Mock
    private RestTemplate restTemplate;

    private WebhookNotificationGateway webhookNotificationGateway;

    private final String testWebhookUrl = "http://test-url.com/webhook";

    @BeforeEach
    void setUp() {
        webhookNotificationGateway = new WebhookNotificationGateway(restTemplate, testWebhookUrl);
    }

    @Test
    @DisplayName("Should call RestTemplate with correct parameters on successful notification")
    void notifyOrderCompleted_shouldCallRestTemplateWithCorrectParameters() {
        Order order = Order.builder().id(UUID.randomUUID()).build();

        webhookNotificationGateway.notifyOrderCompleted(order);

        verify(restTemplate).postForEntity(testWebhookUrl, order, String.class);
    }

    @Test
    @DisplayName("Should catch exception and not throw when RestTemplate fails")
    void notifyOrderCompleted_shouldHandleExceptionWhenRestTemplateFails() {
        Order order = Order.builder().id(UUID.randomUUID()).build();
        doThrow(new RestClientException("Service Unavailable")).when(restTemplate).postForEntity(testWebhookUrl, order, String.class);

        assertDoesNotThrow(() -> webhookNotificationGateway.notifyOrderCompleted(order));
    }
}