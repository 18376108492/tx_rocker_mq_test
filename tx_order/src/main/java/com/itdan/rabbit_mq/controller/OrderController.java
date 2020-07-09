package com.itdan.rabbit_mq.controller;

import com.itdan.rabbit_mq.entry.Order;
import com.itdan.rabbit_mq.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private  IOrderService iOrderService;

    @PostMapping
    public String addOrder(@RequestBody Order order) throws Exception {
        boolean result= iOrderService.addOrder4MQ(order);
       if(result){
           return "添加订单成功";
       }
       return "添加订单失败";
    }


}
