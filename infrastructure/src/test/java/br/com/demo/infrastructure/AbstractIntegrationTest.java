package br.com.demo.infrastructure;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    private static final MongoDBContainer MONGO_DB_CONTAINER =
            new MongoDBContainer("mongo:latest")
                    .withExposedPorts(27017);

    @Container
    private static final RabbitMQContainer RABBITMQ_CONTAINER =
            new RabbitMQContainer("rabbitmq:3-management")
                    .withExposedPorts(5672, 15672);

    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry) {
        // Configuração do MongoDB
        final var mongoUri = "mongodb://%s:%d/test".formatted(
                MONGO_DB_CONTAINER.getHost(),
                MONGO_DB_CONTAINER.getMappedPort(27017)
        );
        registry.add("spring.data.mongodb.uri", () -> mongoUri);

        // Configuração do RabbitMQ
        registry.add("spring.rabbitmq.host", RABBITMQ_CONTAINER::getHost);
        registry.add("spring.rabbitmq.port", () -> RABBITMQ_CONTAINER.getMappedPort(5672));
        registry.add("spring.rabbitmq.username", RABBITMQ_CONTAINER::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBITMQ_CONTAINER::getAdminPassword);
    }
}