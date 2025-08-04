package br.com.demo.infrastructure.config;

import br.com.demo.infrastructure.mapper.MoneyMapper;
import br.com.demo.infrastructure.mapper.MoneyMapperImpl;
import br.com.demo.infrastructure.mapper.OrderMapper;
import br.com.demo.infrastructure.mapper.OrderMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public OrderMapper orderMapper() {
        return new OrderMapperImpl();
    }

    @Bean
    public MoneyMapper moneyMapper() {
        return new MoneyMapperImpl();
    }

}
