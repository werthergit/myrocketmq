package com.example.rocketmq.consumer;

import com.example.rocketmq.order.model.Order;
import com.example.rocketmq.order.service.OrderService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

/**
 * @Author: Xiaour
 * @Description:消费者
 * @Date: 2018/8/9 14:51
 */

@Component
public class ConsumerOrder implements CommandLineRunner {

    /**
     * 消费者
     */
    @Value("${apache.rocketmq.consumer.pushConsumer}")
    private String pushConsumer;

    /**
     * NameServer 地址
     */
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Autowired
    public OrderService orderService;

    /**
     * 初始化RocketMq的监听信息，渠道信息
     */
    public void messageListener(){

        DefaultMQPushConsumer consumer=new DefaultMQPushConsumer("SpringBootRocketMqGroup_order");

        consumer.setNamesrvAddr(namesrvAddr);
        try {

            // 订阅PushTopic下Tag为push的消息,都订阅消息
            consumer.subscribe("PushTopic", "push");

            // 程序第一次启动从消息队列头获取数据
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            //可以修改每次消费消息的数量，默认设置是每次消费一条
            consumer.setConsumeMessageBatchMaxSize(1);

            //在此监听中消费信息，并返回消费的状态信息 - 顺序消费
            consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {

                // 会把不同的消息分别放置到不同的队列中
                for(Message msg:msgs){
                    /*try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
               try {
                   int orderid = Integer.parseInt(new String(msg.getBody()));

                   Order order = new Order();
                   order.setUserId((long) orderid);
                   order.setPrice(10000);
                   order.setStatus((byte) 0);
                   order.setProductId(200L);
                   orderService.inset(order);
               }catch (Exception ex){
                   ex.printStackTrace();
               }finally {
                   System.out.println(OffsetDateTime.now()+"接收到了消息----order："+new String(msg.getBody()));
               }


                }
                return ConsumeOrderlyStatus.SUCCESS;
            });

            consumer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        this.messageListener();
    }
}
