package io.github.regulacao_marcarcao.regulacao_marcacao.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "regional_topic_exchange";

 
    @Bean
    public TopicExchange regionalExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }


    
    @Bean
    public Queue muncipioQueue(@Value("${app.municipio.queue-name}") String queueName) {
        return new Queue(queueName, true);
    }

 
    
    @Bean
    public Binding binding(Queue muncipioQueue, TopicExchange regionalExchange, @Value("${app.municipio.nome-identificador}") String nomeMunicipio) {
        String routingKey = String.format("encaminhamento.%s.#", nomeMunicipio.toUpperCase());
        return BindingBuilder.bind(muncipioQueue).to(regionalExchange).with(routingKey);
    }

    @Bean
    public Binding bindingConvite(Queue muncipioQueue, TopicExchange regionalExchange, @Value("${app.municipio.nome-identificador}") String nomeMunicipio) {
        String routingKey = String.format("convite.%s.#", nomeMunicipio.toUpperCase());
        return BindingBuilder.bind(muncipioQueue).to(regionalExchange).with(routingKey);
    }

    @Bean
    public Binding bindingConviteAceite(Queue muncipioQueue, TopicExchange regionalExchange, @Value("${app.municipio.nome-identificador}") String nomeMunicipio) {
        String routingKey = String.format("convite-aceite.%s.#", nomeMunicipio.toUpperCase());
        return BindingBuilder.bind(muncipioQueue).to(regionalExchange).with(routingKey);
    }

    /**
     * Define o conversor de mensagens para JSON.
     * Essencial para que o Spring consiga serializar/desserializar os DTOs.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
