package br.com.demo.infrastructure.api;

import br.com.demo.infrastructure.AbstractIntegrationTest;
import br.com.demo.infrastructure.persistence.repository.OrderMongoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static java.util.concurrent.TimeUnit.*;
import static org.awaitility.Awaitility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class OrdersApiIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderMongoRepository orderMongoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanup() {
        // Garante que a base de dados está limpa antes de cada teste
        orderMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um pedido com sucesso, processá-lo de forma assíncrona e guardá-lo na base de dados")
    void givenAValidOrderRequest_whenPostOrders_shouldProcessAndSaveOrder() throws Exception {
        // given
        final var externalOrderId = UUID.randomUUID().toString();
        final var requestBody = """
                {
                  "externalOrderId": "%s",
                  "items": [
                    {
                      "productCode": "P001",
                      "productName": "Product A",
                      "quantity": 1,
                      "unitPrice": 50.00
                    }
                  ]
                }
                """.formatted(externalOrderId);

        assertEquals(0, orderMongoRepository.count());

        // when
        final var request = post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        this.mockMvc.perform(request)
                .andExpect(status().isCreated()) // A resposta da API deve ser imediata
                .andExpect(header().exists("Location"));

        // then
        // Como o processamento é assíncrono, esperamos até que o pedido apareça na base de dados.
        await().atMost(10, SECONDS).until(() -> orderMongoRepository.count() > 0);

        // Verificamos se o pedido foi guardado corretamente após ser consumido pelo listener.
        final var savedOrder = orderMongoRepository.findByExternalOrderId(externalOrderId).get();

        assertEquals(1, orderMongoRepository.count());
    }
}