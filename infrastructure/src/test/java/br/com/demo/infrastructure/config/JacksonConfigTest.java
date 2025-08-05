package br.com.demo.infrastructure.config;

import br.com.demo.domain.valueobject.Money;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JacksonConfig")
class JacksonConfigTest {

    private JacksonConfig jacksonConfig;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jacksonConfig = new JacksonConfig();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(jacksonConfig.moneyModule());
    }

    @Nested
    @DisplayName("Money Serialization")
    class MoneySerialization {

        @Test
        @DisplayName("Should correctly serialize Money object to a number")
        void shouldCorrectlySerializeMoney() throws JsonProcessingException {
            Money money = Money.of(new BigDecimal("199.99"));

            String json = objectMapper.writeValueAsString(money);

            assertThat(json).isEqualTo("199.99");
        }

        @Test
        @DisplayName("Should serialize null Money object to null")
        void shouldSerializeNullMoneyToNull() throws JsonProcessingException {
            String json = objectMapper.writeValueAsString(null);

            assertThat(json).isEqualTo("null");
        }
    }

    @Nested
    @DisplayName("Money Deserialization")
    class MoneyDeserialization {
        @Test
        @DisplayName("Should correctly deserialize a number to a Money object")
        void shouldCorrectlyDeserializeMoney() throws JsonProcessingException {
            String json = "123.45";
            Money expectedMoney = Money.of(new BigDecimal("123.45"));

            Money actualMoney = objectMapper.readValue(json, Money.class);

            assertThat(actualMoney).isEqualTo(expectedMoney);
        }
    }
}