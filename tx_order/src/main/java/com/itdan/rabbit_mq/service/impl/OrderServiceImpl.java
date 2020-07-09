package com.itdan.rabbit_mq.service.impl;

import com.itdan.rabbit_mq.entry.Order;
import com.itdan.rabbit_mq.mapper.OrderMapper;
import com.itdan.rabbit_mq.utils.MQClientInfo;
import com.itdan.rabbit_mq.utils.MQProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itdan.rabbit_mq.service.IOrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderServiceImpl implements IOrderService {

    private Logger logger =LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MQClientInfo mqClientInfo;

    private RestTemplate template = new RestTemplate();

    @Transactional
    public boolean  addOrder(Order order){
        logger.info("添加订单方法参数:{}",order);
        int row= orderMapper.insert(order);

        //先通过RestTemplate来做远程调用，后面会换成Feign远程调用
       if(row == 1){
           //通过接口调用库存系统，修改库存。
           String url = "http://localhost:8082/product/updateProduct?productNo="+order.getProductNo()+"&number="+order.getNumber();
           String result_2=template.getForEntity(url, String.class).getBody();
           logger.info("远程调用减少商品数量事务结果:{}",result_2);
            return true;
       }
       return  false;
    }

    @Transactional
    @Override
    public boolean addOrder4MQ(Order order) throws Exception {
        logger.info("添加订单方法参数:{}",order);
        int row= orderMapper.insert(order);

        //使用MQ来传递消息
        if(row>0){
            Channel channel=  mqClientInfo.getConnection().createChannel();
           try{
               //开启MQ事物
               channel.txSelect();
               logger.info("发送订单数据消息");
               //添加订单成功后，将订单信息保存至MQ中，商品系统通过MQ中获取数据修改相应的数据即可
               channel.basicPublish(MQProperties.EXCHANGE_NAME_TX, MQProperties.ROUTE_KEY_TX, MessageProperties.PERSISTENT_TEXT_PLAIN, order.toString().getBytes()); //发送消息
               //提交事物
               channel.txCommit();
           }catch (Exception e){
               //如果消息传递失败，回滚MQ事物
               channel.txRollback();
               logger.error(e.getMessage());
           }
           return  true;
        }
        return false;
    }

    /**
     * 订单添加失败回滚操作
     * @param orderNo
     */
    @Transactional
    @Override
    public void addOrderRollback(Integer orderNo){
        logger.info("订单添加失败回滚操作-删除订单操作参数:{}",orderNo);
        orderMapper.delete(orderNo);
    }

    /**
     *  订单添加成功提交操作
     * @param orderNo
     */
    @Override
    public void addOrderCommit(Integer orderNo){
         logger.info("订单添加成功提交操作");
         orderMapper.update(orderNo);
    }




}
