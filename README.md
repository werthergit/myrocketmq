# myrocketmq


一、三种消费 ：1.普通消费 2. 顺序消费 3.事务消费

1.1  顺序消费：在网购的时候，我们需要下单，那么下单需要假如有三个顺序，第一、创建订单 ，第二：订单付款，第三：订单完成。也就是这个三个环节要有顺序，这个订单才有意义。RocketMQ可以保证顺序消费，他的实现是生产者（一个生产者可以对多个主题去发送消息）将这个三个消息放在topic（一个topic默认有4个队列）的一个队列里面，单机支持上万个持久化队列，消费端去消费的时候也是只能有一个Consumer去取得这个队列里面的数据，然后顺序消费。

单个节点（Producer端1个、Consumer端1个）
略
多个节点（Producer端1个、Consumer端2个）  
ProducerMultiple
Consumer1Multiple
Consumer2Multiple

https://blog.csdn.net/gwd1154978352/article/details/80802674

https://blog.csdn.net/u010634288/article/details/57158374/
默认的是 集群消费模式