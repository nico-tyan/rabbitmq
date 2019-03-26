package com.nico.sender;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class HelloReceive {

//    @RabbitListener(queues="queue")    //监听器监听指定的Queue
//    public void process(@Payload String message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel)throws Exception  {
//        System.out.println("消息:"+str);
//    }
    
    @RabbitListener(queues="topic.message")    //监听器监听指定的Queue
    public void process1(String str) {  
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("message:"+str);
    }
    
    //设定其他消费监听器
    @RabbitListener(queues="topic.messages")    //监听器监听指定的Queue
    public void process2(String str,CorrelationData correlationId) {
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	System.out.println("correlationId:"+correlationId.getId()+" : "+correlationId.toString());
        System.out.println("messages:"+str);
    }
    
//    @RabbitListener(queues="topic.message")    //监听器监听指定的Queue
//    public void process3(String str) {    
//        System.out.println("message3:"+str);
//    }
//    
//    @RabbitListener(queues="topic.message")    //监听器监听指定的Queue
//    public void process4(String str) {    
//        System.out.println("message4:"+str);
//    }
//    
//    @RabbitListener(queues="topic.message")    //监听器监听指定的Queue
//    public void process5(String str) {    
//        System.out.println("message5:"+str);
//    }
//    
//    @RabbitListener(queues="topic.message")    //监听器监听指定的Queue
//    public void process6(String str) {    
//        System.out.println("message6:"+str);
//    }

}