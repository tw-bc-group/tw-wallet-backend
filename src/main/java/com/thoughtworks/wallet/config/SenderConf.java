package com.thoughtworks.wallet.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SenderConf {

    @Bean(name = "ExampleMessage")
    public Queue ExampleMessage() {
        return new Queue("fanout.A", true);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("exampleExchange");
    }

    @Bean
    public Binding bindingExchangeA(@Qualifier("ExampleMessage") Queue ExampleMessage, TopicExchange topicExchange) {
        return BindingBuilder.bind(ExampleMessage).to(topicExchange).with("example");
    }
}
