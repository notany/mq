package com.rabbitmq.demo.exchang.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.client.MyConsumer;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 14:19
 * @description 消费者2
 **/
public class FanoutConsumerTwo {

    // 队列
    private static final String QUEUE = "test.fanout.queue.two";
    // 交换机
    private static final String EXCHANG_NAME = "test.fanout.exchang";

    public static void main(String args[]) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        // 4、创建交换机、队列并绑定
        channel.exchangeDeclare(EXCHANG_NAME, BuiltinExchangeType.FANOUT);
        // 创建队列
        channel.queueDeclare(QUEUE, false, false, false, null);
        // 绑定交换机,不需要路由也能投递
        channel.queueBind(QUEUE, EXCHANG_NAME, "");
        // 5、创建消费者
        MyConsumer consumer = new MyConsumer(channel);
        channel.basicConsume(QUEUE, true, consumer);
    }
}
