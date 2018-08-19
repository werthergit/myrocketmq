package com.example.rocketmq.order.service;

import com.example.rocketmq.order.mapper.OrderMapper;
import com.example.rocketmq.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by my on 2018/8/19.
 */
@Service
public class OrderService {

    @Autowired
    public OrderMapper orderMapper;

    public void inset(Order order){
        orderMapper.insertSelective(order);
    }

}
