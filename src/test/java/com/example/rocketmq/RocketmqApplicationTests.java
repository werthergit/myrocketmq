package com.example.rocketmq;

import com.example.rocketmq.order.model.Order;
import com.example.rocketmq.order.service.OrderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RocketmqApplicationTests {


	@Autowired
	public OrderService orderService;

	private final int count = 500;

	private RestTemplate restTemplate = new RestTemplate();
	/*
 * 线程计数器
 * 	将线程数量初始化
 * 	每执行完成一条线程，调用countDown()使计数器减1
 * 	主线程调用方法await()使其等待，当计数器为0时才被执行
 */
	private CountDownLatch latch = new CountDownLatch(count);

	/**
	 * mysql> show variables like "max_connections";
	 +-----------------+-------+
	 | Variable_name   | Value |
	 +-----------------+-------+
	 | max_connections | 10    |
	 +-----------------+-------+
	 1 row in set

	 mysql>  set GLOBAL max_connections=100;
	 */
	@Test
	public void contextLoads() {

		for(int price=0;price<count;price++) {
			int finalPrice = price;
			new Thread(() -> {
				System.out.println("In Java8, Lambda expression rocks !!"+finalPrice);
				add(finalPrice);
				//Assert.assertEquals(add(finalPrice), 1);
				//System.out.println("----------over----");
				latch.countDown(); // 执行完毕，计数器减1
			}).start();
		}


//		for (int price = 0; price < count; price++) {
//			int finalPrice = price;
//			Runnable runnable = new Runnable() {
//				public void run() {
//					try {
//						//Thread.sleep(1000);
//						add(finalPrice);
//						System.out.println("-----------user:"+Thread.currentThread()+"---"+ finalPrice);
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					} finally {
//						latch.countDown(); // 执行完毕，计数器减1
//					}
//				}
//			};
//			new Thread(runnable, "【自定义线程】").start();
//		}
		try {
			latch.await(); // 主线程等待
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

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
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void contextLoads3() {

		for(int price=0;price<count;price++) {
			new Thread(new MyTestThread()).start();
			System.out.println("In Java8, Lambda expression rocks !!"+price);
			latch.countDown(); // 执行完毕，计数器减1
		}

		try {
			Thread.currentThread().sleep(13000);
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

	public class MyTestThread implements Runnable{

		@Override
		public void run() {
			try {
				latch.await(); // 线程等待
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//add(123);
			restTemplate.getForEntity("http://localhost:8080/order/add", String.class).getBody();
		}
	}

}
