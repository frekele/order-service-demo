package br.com.demo.infrastructure.config.json;

import br.com.demo.domain.valueobject.Money;
import com.fasterxml.jackson.core.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoneyDeserializerTest {

    @Mock
    private JsonParser jsonParser;

    private MoneyDeserializer moneyDeserializer;

    @BeforeEach
    void setUp() {
        moneyDeserializer = new MoneyDeserializer();
    }

    @Test
    void shouldDeserializeDecimalValueToMoney() throws IOException {
        BigDecimal decimalValue = new BigDecimal("199.99");
        when(jsonParser.getDecimalValue()).thenReturn(decimalValue);

        Money result = moneyDeserializer.deserialize(jsonParser, null);

        assertEquals(Money.of(decimalValue), result);
    }

    @Test
    void shouldDeserializeNullToMoneyZero() throws IOException {
        when(jsonParser.getDecimalValue()).thenReturn(null);

        Money result = moneyDeserializer.deserialize(jsonParser, null);

        assertEquals(Money.zero(), result);
    }

    @Test
    void shouldThrowIOExceptionWhenParserThrowsIOException() throws IOException {
        when(jsonParser.getDecimalValue()).thenThrow(new IOException("Parser error"));

        assertThrows(IOException.class, () -> moneyDeserializer.deserialize(jsonParser, null));
    }
}