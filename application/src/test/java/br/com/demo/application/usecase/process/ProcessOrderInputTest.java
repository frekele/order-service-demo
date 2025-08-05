package br.com.demo.application.usecase.process;

import br.com.demo.domain.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProcessOrderInputTest {

    @Mock
    private Order order;

    @Mock
    private Order anotherOrder;

    @Test
    @DisplayName("Should correctly create an instance and return the order via accessor")
    void shouldCreateInputAndGetOrder() {
        var input = new ProcessOrderInput(order);

        assertNotNull(input);
        assertEquals(order, input.order());
    }

    @Test
    @DisplayName("Should correctly implement equals and hashCode based on its properties")
    void testEqualsAndHashCode() {
        var input1 = new ProcessOrderInput(order);
        var input2 = new ProcessOrderInput(order);
        var input3 = new ProcessOrderInput(anotherOrder);

        assertEquals(input1, input2, "Instances with the same order should be equal");
        assertEquals(input1.hashCode(), input2.hashCode(), "Hash codes for equal instances should be the same");

        assertNotEquals(input1, input3, "Instances with different orders should not be equal");
        assertNotEquals(input1, null, "Instance should not be equal to null");
        assertNotEquals(input1, new Object(), "Instance should not be equal to an object of a different type");
    }
}