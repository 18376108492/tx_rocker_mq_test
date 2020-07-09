package com.itdan.rabbit_mq.service;

import com.itdan.rabbit_mq.entry.Order;
import org.springframework.transaction.annotation.Transactional;

public interface IOrderService {

    /**
     * 添加订单方法
     * @param order
     * @return
     */
     boolean  addOrder(Order order);

    /**
     * 添加订单方法并通过MQ去通知商品系统减少商品数量
     * @param order
     * @return
     */
     boolean addOrder4MQ(Order order) throws Exception;

    /**
     * 订单添加失败回滚操作
     * @param orderNo
     */
     void addOrderRollback(Integer orderNo);


    /**
     *  订单添加成功提交操作
     * @param orderNo
     */
     void addOrderCommit(Integer orderNo);
}
