package com.example.rocketmq;

import com.example.rocketmq.order.model.Order;
import com.example.rocketmq.order.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RocketmqApplicationTests2 {


	@Autowired
	public OrderService orderService;

	private final int count = 200;

	private RestTemplate restTemplate = new RestTemplate();
	/*
 * 线程计数器
 * 	将线程数量初始化
 * 	每执行完成一条线程，调用countDown()使计数器减1
 * 	主线程调用方法await()使其等待，当计数器为0时才被执行
 */
	private CountDownLatch latch = new CountDownLatch(count);


	@Test
	public void contextLoads2() {

		for(int price=0;price<count;price++) {
			int finalPrice = price;
			new Thread(() -> {
				try {
					latch.await(); // 主线程等待
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				add(finalPrice);
				//Assert.assertEquals(add(finalPrice), 1);
				//System.out.println("----------over----");
			}).start();
			System.out.println("In Java8, Lambda expression rocks !!"+finalPrice);
			latch.countDown(); // 执行完毕，计数器减1
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



	public int add(int price) {
		Order order = new Order();
		order.setUserId(1000L);
		order.setPrice(price);
		order.setStatus((byte) 0);
		order.setProductId(200L);
		int i = orderService.inset(order);
		return i;
	}


}
