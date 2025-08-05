package br.com.demo.infrastructure.config.json;

import br.com.demo.domain.valueobject.Money;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoneySerializerTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    private MoneySerializer moneySerializer;

    @BeforeEach
    void setUp() {
        moneySerializer = new MoneySerializer();
    }

    @Test
    @DisplayName("Should serialize a non-null Money object to its BigDecimal amount")
    void shouldSerializeNonNullMoney() throws IOException {
        Money money = Money.of(new BigDecimal("199.99"));

        moneySerializer.serialize(money, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(money.getAmount());
    }

    @Test
    @DisplayName("Should do nothing when serializing a null Money object")
    void shouldHandleNullMoney() throws IOException {
        moneySerializer.serialize(null, jsonGenerator, serializerProvider);

        verifyNoInteractions(jsonGenerator);
    }
}