package br.com.demo.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import static org.assertj.core.api.Assertions.*;

class RabbitMQConfigTest {

    private RabbitMQConfig rabbitMQConfig;

    @BeforeEach
    void setUp() {
        rabbitMQConfig = new RabbitMQConfig();
    }

    @Test
    @DisplayName("should create the main topic exchange with the correct name")
    void shouldCreateTopicExchange() {
        TopicExchange exchange = rabbitMQConfig.exchange();
        assertThat(exchange.getName()).isEqualTo(RabbitMQConfig.EXCHANGE_NAME);
    }

    @Test
    @DisplayName("should create the dead-letter topic exchange with the correct name")
    void shouldCreateDeadLetterExchange() {
        TopicExchange deadLetterExchange = rabbitMQConfig.deadLetterExchange();
        assertThat(deadLetterExchange.getName()).isEqualTo(RabbitMQConfig.DEAD_LETTER_EXCHANGE_NAME);
    }

    @Test
    @DisplayName("should create a durable dead-letter queue with the correct name")
    void shouldCreateDeadLetterQueue() {
        Queue deadLetterQueue = rabbitMQConfig.deadLetterQueue();
        assertThat(deadLetterQueue.getName()).isEqualTo(RabbitMQConfig.DEAD_LETTER_QUEUE_NAME);
        assertThat(deadLetterQueue.isDurable()).isTrue();
    }

    @Test
    @DisplayName("should create the binding for the dead-letter queue and exchange")
    void shouldCreateDeadLetterBinding() {
        Queue deadLetterQueue = rabbitMQConfig.deadLetterQueue();
        TopicExchange deadLetterExchange = rabbitMQConfig.deadLetterExchange();

        Binding deadLetterBinding = rabbitMQConfig.deadLetterBinding(deadLetterQueue, deadLetterExchange);

        assertThat(deadLetterBinding.getExchange()).isEqualTo(RabbitMQConfig.DEAD_LETTER_EXCHANGE_NAME);
        assertThat(deadLetterBinding.getDestination()).isEqualTo(RabbitMQConfig.DEAD_LETTER_QUEUE_NAME);
        assertThat(deadLetterBinding.getRoutingKey()).isEqualTo("#");
    }

    @Test
    @DisplayName("should create a durable main queue with dead-lettering configured")
    void shouldCreateQueueWithDeadLetterExchange() {
        Queue queue = rabbitMQConfig.queue();
        assertThat(queue.getName()).isEqualTo(RabbitMQConfig.QUEUE_NAME);
        assertThat(queue.isDurable()).isTrue();
        assertThat(queue.getArguments()).containsEntry("x-dead-letter-exchange", RabbitMQConfig.DEAD_LETTER_EXCHANGE_NAME);
    }

    @Test
    @DisplayName("should create the binding for the main queue and exchange")
    void shouldCreateBinding() {
        Queue queue = rabbitMQConfig.queue();
        TopicExchange exchange = rabbitMQConfig.exchange();

        Binding binding = rabbitMQConfig.binding(queue, exchange);

        assertThat(binding.getExchange()).isEqualTo(RabbitMQConfig.EXCHANGE_NAME);
        assertThat(binding.getDestination()).isEqualTo(RabbitMQConfig.QUEUE_NAME);
        assertThat(binding.getRoutingKey()).isEqualTo(RabbitMQConfig.ROUTING_KEY);
    }

    @Test
    @DisplayName("should create a Jackson2JsonMessageConverter")
    void shouldCreateJsonMessageConverter() {
        MessageConverter messageConverter = rabbitMQConfig.jsonMessageConverter(new ObjectMapper());
        assertThat(messageConverter).isInstanceOf(Jackson2JsonMessageConverter.class);
    }
}