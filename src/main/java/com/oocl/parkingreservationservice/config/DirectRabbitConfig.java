package com.oocl.parkingreservationservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectRabbitConfig {

    public static final String QUEUE_NAME = "NotificationSendSMS";
    public static final String EXCHANGE_NAME = "SmsDirectExchange";
    public static final String ROUTING_KEY = "SmsDirectRouting";
    public static final String LONELY_DIRECT_EXCHANGE = "lonelyDirectExchange";

    @Bean
    public Queue TestDirectQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    DirectExchange TestDirectExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(TestDirectQueue()).to(TestDirectExchange()).with(ROUTING_KEY);
    }

    @Bean
    DirectExchange lonelyDirectExchange() {
        return new DirectExchange(LONELY_DIRECT_EXCHANGE);
    }

}