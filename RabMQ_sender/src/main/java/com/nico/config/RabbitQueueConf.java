package com.nico.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
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
    
    public static final String DELAY_QUEUE_PER_MESSAGE_TTL_NAME="delay_queue_per_message_ttl";
    public static final String DELAY_EXCHANGE_NAME="delay_exchange";
    public static final String DELAY_PROCESS_QUEUE_NAME="delay_process_queue";
    
    //delay_queue_per_message_ttl的配置代码： TTL配置在消息上的缓冲队列。
    @Bean
    Queue delayQueuePerMessageTTL() { 
    	return QueueBuilder.durable(DELAY_QUEUE_PER_MESSAGE_TTL_NAME) 
    			.withArgument("x-dead-letter-exchange", DELAY_EXCHANGE_NAME) 
    			// DLX，dead letter发送到的exchange 
    			.withArgument("x-dead-letter-routing-key", DELAY_PROCESS_QUEUE_NAME) 
    			// dead letter携带的routing key 
    			.build(); 
    }
    
    public static final String DELAY_QUEUE_PER_QUEUE_TTL_NAME="delay_queue_per_queue_ttl";
    public static final String QUEUE_EXPIRATION="QUEUE_EXPIRATION";
    //delay_queue_per_queue_ttl的配置代码：TTL配置在队列上的缓冲队列。
    @Bean 
    Queue delayQueuePerQueueTTL() { 
    	return QueueBuilder.durable(DELAY_QUEUE_PER_QUEUE_TTL_NAME) 
    		.withArgument("x-dead-letter-exchange", DELAY_EXCHANGE_NAME) 
    		// DLX .withArgument("x-dead-letter-routing-key", DELAY_PROCESS_QUEUE_NAME) 
    		// dead letter携带的routing key 
    		.withArgument("x-message-ttl", QUEUE_EXPIRATION) 
		    // 设置队列的过期时间 
		    .build(); 
    }
    
    //delay_process_queue的配置最为简单：际消费队列。
    @Bean(name="delayProcessQueue") 
    Queue delayProcessQueue() { 
    	return QueueBuilder.durable(DELAY_PROCESS_QUEUE_NAME) .build(); 
    }
    
    //指定路由交换机
    /**
     * 任何发送到Direct Exchange的消息都会被转发到RouteKey中指定的Queue。
		1.一般情况可以使用rabbitMQ自带的Exchange：””(该Exchange的名字为空字符串，下文称其为default Exchange)。
		2.这种模式下不需要将Exchange进行任何绑定(binding)操作
		3.消息传递时需要一个“RouteKey”，可以简单的理解为要发送到的队列名字。
		4.如果vhost中不存在RouteKey中指定的队列名，则该消息会被抛弃。 
     */
    @Bean 
    DirectExchange delayExchange() { 
    	return new DirectExchange(DELAY_EXCHANGE_NAME); 
    }
    
    @Bean Binding dlxBinding(@Qualifier("delayProcessQueue") Queue delayProcessQueue, DirectExchange delayExchange) { 
    	return BindingBuilder.bind(delayProcessQueue) .to(delayExchange) .with(DELAY_PROCESS_QUEUE_NAME); 
    }
    
    //模糊匹配路由交换机
    /**
     * 任何发送到Topic Exchange的消息都会被转发到所有关心RouteKey中指定话题的Queue上
		1.这种模式较为复杂，简单来说，就是每个队列都有其关心的主题，所有的消息都带有一个“标题”(RouteKey)，Exchange会将消息转发到所有关注主题能与RouteKey模糊匹配的队列。
		2.这种模式需要RouteKey，也许要提前绑定Exchange与Queue。
		3.在进行绑定时，要提供一个该队列关心的主题，如“#.log.#”表示该队列关心所有涉及log的消息(一个RouteKey为”MQ.log.error”的消息会被转发到该队列)。
		4.“#”表示0个或若干个关键字，“”表示一个关键字。如“log.”能与“log.warn”匹配，无法与“log.warn.timeout”匹配；但是“log.#”能与上述两者匹配。
		5.同样，如果Exchange没有发现能够与RouteKey匹配的Queue，则会抛弃此消息。
     */
    @Bean(name="topic_exchange")
    public TopicExchange exchange() {
        return new TopicExchange("topic_exchange");
    }
    
    
}
