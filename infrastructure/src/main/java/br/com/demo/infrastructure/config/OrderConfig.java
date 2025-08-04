package br.com.demo.infrastructure.config;

import br.com.demo.application.gateway.OrderGateway;
import br.com.demo.application.usecase.create.CreateOrderUseCase;
import br.com.demo.application.usecase.create.DefaultCreateOrderUseCase;
import br.com.demo.infrastructure.mapper.OrderMapper;
import br.com.demo.infrastructure.mapper.OrderMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

    @Bean
    public OrderMapper orderMapper(){
        return new OrderMapperImpl();
    }

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderGateway orderGateway) {
        return new DefaultCreateOrderUseCase(orderGateway);
    }

}
