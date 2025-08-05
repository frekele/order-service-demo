package br.com.demo.infrastructure;

import br.com.demo.domain.valueobject.Money;
import br.com.demo.infrastructure.config.json.MoneyDeserializer;
import br.com.demo.infrastructure.config.json.MoneySerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Module moneyModule() {
        SimpleModule module = new SimpleModule("MoneyModule");
        module.addSerializer(Money.class, new MoneySerializer());
        module.addDeserializer(Money.class, new MoneyDeserializer());
        return module;
    }
}