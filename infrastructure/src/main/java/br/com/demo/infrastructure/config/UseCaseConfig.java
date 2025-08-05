package br.com.demo.infrastructure.config;

import br.com.demo.application.gateway.NotificationGateway;
import br.com.demo.application.gateway.OrderEventGateway;
import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.application.usecase.cancel.CancelOrderUseCase;
import br.com.demo.application.usecase.cancel.DefaultCancelOrderUseCase;
import br.com.demo.application.usecase.create.CreateOrderUseCase;
import br.com.demo.application.usecase.create.DefaultCreateOrderUseCase;
import br.com.demo.application.usecase.get.DefaultGetOrderByIdUseCase;
import br.com.demo.application.usecase.get.GetOrderByIdUseCase;
import br.com.demo.application.usecase.list.DefaultListOrdersUseCase;
import br.com.demo.application.usecase.list.ListOrdersUseCase;
import br.com.demo.application.usecase.process.DefaultProcessOrderUseCase;
import br.com.demo.application.usecase.process.ProcessOrderUseCase;
import br.com.demo.application.usecase.retry.DefaultRetryOrderUseCase;
import br.com.demo.application.usecase.retry.RetryOrderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UseCaseConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderGateway orderGateway, OrderEventGateway orderEventGateway) {
        return new DefaultCreateOrderUseCase(orderGateway, orderEventGateway);
    }

    @Bean
    public GetOrderByIdUseCase getOrderByIdUseCase(OrderGateway orderGateway) {
        return new DefaultGetOrderByIdUseCase(orderGateway);
    }

    @Bean
    public ListOrdersUseCase listOrdersUseCase(OrderGateway orderGateway) {
        return new DefaultListOrdersUseCase(orderGateway);
    }

    @Bean
    public CancelOrderUseCase cancelOrderUseCase(OrderGateway orderGateway) {
        return new DefaultCancelOrderUseCase(orderGateway);
    }

    @Bean
    public RetryOrderUseCase retryOrderUseCase(OrderGateway orderGateway) {
        return new DefaultRetryOrderUseCase(orderGateway);
    }

    @Bean
    public ProcessOrderUseCase processOrderUseCase(OrderGateway orderGateway, NotificationGateway notificationGateway) {
        return new DefaultProcessOrderUseCase(orderGateway, notificationGateway);
    }
}
