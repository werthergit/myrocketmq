package com.example.rocketmq.order.controller;

import com.example.rocketmq.order.model.Order;
import com.example.rocketmq.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by my on 2018/8/19.
 */
@Controller
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    public OrderService orderService;

    @ResponseBody
    @RequestMapping(value = "/add")
    public int add(){
        Order order = new Order();
        order.setUserId(1000L);
        order.setPrice(10000);
        order.setStatus((byte) 0);
        order.setProductId(200L);
        return orderService.inset(order);
    }

}
