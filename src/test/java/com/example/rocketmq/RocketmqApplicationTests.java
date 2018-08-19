package com.example.rocketmq;

import com.example.rocketmq.order.model.Order;
import com.example.rocketmq.order.service.OrderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RocketmqApplicationTests {


	@Autowired
	public OrderService orderService;

	private int count = 100;
	/*
 * 线程计数器
 * 	将线程数量初始化
 * 	每执行完成一条线程，调用countDown()使计数器减1
 * 	主线程调用方法await()使其等待，当计数器为0时才被执行
 */
	private CountDownLatch latch = new CountDownLatch(count);

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
