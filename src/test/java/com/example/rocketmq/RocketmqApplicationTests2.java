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
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RocketmqApplicationTests2 {


	@Autowired
	public OrderService orderService;

	private final int count = 200;

	private 	static AtomicInteger atomic = new AtomicInteger(0);

	private RestTemplate restTemplate = new RestTemplate();
	/*
 * 线程计数器
 * 	将线程数量初始化
 * 	每执行完成一条线程，调用countDown()使计数器减1
 * 	主线程调用方法await()使其等待，当计数器为0时才被执行
 */
	private CountDownLatch latch = new CountDownLatch(count);


	@Test
	public void contextLoads3() {

		for(int price=0;price<count;price++) {
			new Thread(new RocketmqApplicationTests2.MyTestThread2()).start();
			System.out.println("In Java8, Lambda expression rocks !!"+price);
			latch.countDown(); // 执行完毕，计数器减1
		}

		try {
			Thread.currentThread().sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



	public class MyTestThread2 implements Runnable{

		@Override
		public void run() {
			try {
				latch.await(); // 线程等待
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//add(123);
			int count = atomic.incrementAndGet();
			restTemplate.getForEntity("http://localhost:8082/push?msg="+count, String.class).getBody();
		}
	}


}
