package br.com.demo.infrastructure.config.json;

import br.com.demo.domain.valueobject.Money;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class MoneyDeserializer extends JsonDeserializer<Money> {
    @Override
    public Money deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        BigDecimal value = p.getDecimalValue();
        return Money.of(value);
    }
}