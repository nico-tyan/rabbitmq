package com.nico.config;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitTemplateCallbackConf implements RabbitTemplate.ConfirmCallback,
RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);            //指定 ConfirmCallback
        rabbitTemplate.setReturnCallback(this); 			//指定 ReturnCallback
    }
    
    //失败消息确认
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
    	System.out.println("----------confirm begin-----------");
    	System.out.println("消息唯一标识："+correlationData);
    	if (ack) {  
            System.out.println("消息发送成功:" + correlationData);  
        } else {  
            System.out.println("消息发送失败:" + cause);  
        }
        System.out.println("----------confirm end-----------");
        System.out.println();
    }
    
    //失败消息返回
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
    	System.out.println("----------returned begin-----------");
    	System.out.println("消息主体 message : "+message);
        System.out.println("消息主体 message : "+replyCode);
        System.out.println("描述："+replyText);
        System.out.println("消息使用的交换器 exchange : "+exchange);
        System.out.println("消息使用的路由键 routing : "+routingKey);
        System.out.println("----------returned end-----------");
        System.out.println();
    }

}

