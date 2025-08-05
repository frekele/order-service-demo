package br.com.demo.infrastructure.config;

import br.com.demo.application.gateway.NotificationGateway;
import br.com.demo.application.gateway.OrderEventGateway;
import br.com.demo.application.gateway.OrderGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UseCaseConfigTest {

    private UseCaseConfig useCaseConfig;

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private OrderEventGateway orderEventGateway;

    @Mock
    private NotificationGateway notificationGateway;

    @BeforeEach
    void setUp() {
        useCaseConfig = new UseCaseConfig();
    }

    @Test
    @DisplayName("Should create RestTemplate bean successfully")
    void shouldCreateRestTemplateBean() {
        final RestTemplate restTemplate = useCaseConfig.restTemplate();
        assertNotNull(restTemplate);
    }

    @Test
    @DisplayName("Should create CreateOrderUseCase bean successfully")
    void shouldCreateCreateOrderUseCaseBean() {
        final var useCase = useCaseConfig.createOrderUseCase(orderGateway, orderEventGateway);
        assertNotNull(useCase);
    }

    @Test
    @DisplayName("Should create GetOrderByIdUseCase bean successfully")
    void shouldCreateGetOrderByIdUseCaseBean() {
        final var useCase = useCaseConfig.getOrderByIdUseCase(orderGateway);
        assertNotNull(useCase);
    }

    @Test
    @DisplayName("Should create ListOrdersUseCase bean successfully")
    void shouldCreateListOrdersUseCaseBean() {
        final var useCase = useCaseConfig.listOrdersUseCase(orderGateway);
        assertNotNull(useCase);
    }

    @Test
    @DisplayName("Should create CancelOrderUseCase bean successfully")
    void shouldCreateCancelOrderUseCaseBean() {
        final var useCase = useCaseConfig.cancelOrderUseCase(orderGateway);
        assertNotNull(useCase);
    }

    @Test
    @DisplayName("Should create RetryOrderUseCase bean successfully")
    void shouldCreateRetryOrderUseCaseBean() {
        final var useCase = useCaseConfig.retryOrderUseCase(orderGateway);
        assertNotNull(useCase);
    }

    @Test
    @DisplayName("Should create ProcessOrderUseCase bean successfully")
    void shouldCreateProcessOrderUseCaseBean() {
        final var useCase = useCaseConfig.processOrderUseCase(orderGateway, notificationGateway);
        assertNotNull(useCase);
    }
}