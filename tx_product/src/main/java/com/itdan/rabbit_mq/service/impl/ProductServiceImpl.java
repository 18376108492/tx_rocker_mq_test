package com.itdan.rabbit_mq.service.impl;

import com.itdan.rabbit_mq.entry.Order;
import com.itdan.rabbit_mq.mapper.ProductMapper;
import com.itdan.rabbit_mq.service.IProductService;
import com.itdan.rabbit_mq.utils.MQProperties;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 修改库存
     **/
    @Transactional
    public int updateProduct(Order order) throws Exception {
        System.out.println("============修改库存");
        int row = productMapper.update(order); //修改库存
        return row;
    }

    @Transactional
    public void updateProduct4MQ(Order order, Channel channel, Message message) throws Exception {
        System.out.println("============修改库存");
        int row = 0;
        try {
            row = productMapper.update(order);
        } catch (Exception e) {
            //消费失败，根据实际业务情况选择是否重新入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            throw e;
        }

        if(row==0){
            //库存系统修改库存失败，则向MQ中发送成功失败ID
            amqpTemplate.convertAndSend(MQProperties.EXCHANGE_NAME_TX_RETURN, MQProperties.ROUTE_KEY_TX_RETURN_FAILURE, order.getOrderNo());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            throw new RuntimeException("============修改库存失败");
        }else{
            //库存系统修改库存成功，则向MQ中发送成功订单ID
            amqpTemplate.convertAndSend(MQProperties.EXCHANGE_NAME_TX_RETURN, MQProperties.ROUTE_KEY_TX_RETURN_SUCCESS, order.getOrderNo());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

}
