package com.itdan.rabbit_mq.utils;

import com.itdan.rabbit_mq.entry.Order;
import com.itdan.rabbit_mq.service.IProductService;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;



@Component
@RabbitListener(queues=MQProperties.QUEUE_NAME_TX)
public class MQBusiness {

	@Autowired
	private IProductService productService;
	
	@RabbitHandler
	public void process(String body, Channel channel, Message message) throws Exception {
		JSONObject obj = new JSONObject(body);
		int orderNO = obj.getInt("orderNO");
		int productNo = obj.getInt("productNo");
		int number = obj.getInt("number");
		Order order = new Order();
		order.setOrderNo(orderNO);
		order.setProductNo(productNo);
		order.setNumber(number);
		productService.updateProduct4MQ(order, channel, message);
	}

}
