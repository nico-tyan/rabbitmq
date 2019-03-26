package com.nico.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitQueueConf {
	@Bean
    public Queue queue() {
         return new Queue("queue");
    }
	
	@Bean(name="message")
    public Queue queueMessage() {
        return new Queue("topic.message");
    }

    @Bean(name="messages")
    public Queue queueMessages() {
    	Queue queue = new Queue("topic.messages",true);
        return queue;
    }
}
