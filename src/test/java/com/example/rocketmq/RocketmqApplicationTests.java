package com.example.rocketmq;

import com.example.rocketmq.order.model.Order;
import com.example.rocketmq.order.service.OrderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RocketmqApplicationTests {


	@Autowired
	public OrderService orderService;

	@Test
	public void contextLoads() {
		new Thread(() -> {
			System.out.println("In Java8, Lambda expression rocks !!");
			add(1024);
			Assert.assertEquals(add(1), 6);
			System.out.println("----------over----");
		}).start();

		for(int price=0;price<10;price++) {
			int finalPrice = price;
			new Thread(() -> {
				System.out.println("In Java8, Lambda expression rocks !!"+finalPrice);
				Assert.assertEquals(add(finalPrice), 1);
				System.out.println("----------over----");
			}).start();
		}
	}

	public int add(int price){
		Order order = new Order();
		order.setUserId(1000L);
		order.setPrice(price);
		order.setStatus((byte) 0);
		order.setProductId(200L);
		int i = orderService.inset(order);
		return i;
	}

}
