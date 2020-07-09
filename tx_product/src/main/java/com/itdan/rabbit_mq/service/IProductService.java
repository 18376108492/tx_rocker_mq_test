package com.itdan.rabbit_mq.service;

import com.itdan.rabbit_mq.entry.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

public interface IProductService {

    void updateProduct4MQ(Order order, Channel channel, Message message) throws Exception;
}
