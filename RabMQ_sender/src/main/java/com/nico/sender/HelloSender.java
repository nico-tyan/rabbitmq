package com.nico.sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelloSender {
    @Autowired
    private RabbitTemplate template;
    
    public void send() {
    	
    	for (int i = 0; i < 50000; i++) {
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		template.convertAndSend("queue","hello,25252~"+i+'号');
		}
    
    }
}