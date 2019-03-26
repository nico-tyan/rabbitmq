package com.nico;

import java.util.Date;
import java.util.UUID;

import org.apache.http.client.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nico.sender.HelloSender;

@SpringBootTest(classes = App.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestMQ {
	@Autowired
	private HelloSender helloSender;
	@Autowired
	private RabbitTemplate template;

//	@Test
//	public void testRabbit() {
//		helloSender.send();
//
//	}

	@Test
	public void testTopic() {
		//方法的第一个参数是交换机名称,第二个参数是发送的key,第三个参数是内容,
		//RabbitMQ将会根据第二个参数去寻找有没有匹配此规则的队列,如果有,则把消息给它,
		//如果有不止一个,则把消息分发给匹配的队列(每个队列都有消息!),
		//显然在我们的测试中,参数2匹配了两个队列,因此消息将会被发放到这两个队列中,
		//而监听这两个队列的监听器都将收到消息!那么如果把参数2改为topic.messages呢?
		//显然只会匹配到一个队列,那么process2方法对应的监听器收到消息!
		for (int i = 0; i < 500; i++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		if(i%2==0){
    			CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString().replaceAll("-", ""));
    			template.convertAndSend("topicExchange", "topic.message",DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"),correlationId);
    		}else{
    			CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString().replaceAll("-", ""));
    			template.convertAndSend("topicExchange", "topic.messages",DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"),correlationId);
    		}
		}
	}
}
