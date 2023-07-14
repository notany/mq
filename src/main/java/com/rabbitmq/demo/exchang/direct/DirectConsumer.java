package com.rabbitmq.demo.exchang.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.client.MyConsumer;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 14:00
 * @description direct交换机消费者
 **/
public class DirectConsumer {

    // 队列
    private static final String QUEUE = "test.direct.queue";
    // 交换机
    private static final String EXCHANG_NAME = "test.direct.exchang";
    // 路由
    private static final String ROUTING_KEY = "test.direct";

    public static void main(String args[]) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        // 4、创建交换机、队列、并绑定
        channel.exchangeDeclare(EXCHANG_NAME, BuiltinExchangeType.DIRECT, true, false, false, null);
        // 创建队列
        channel.queueDeclare(QUEUE, true, false, false, null);
        // 队列、交换机和路由绑定
        channel.queueBind(QUEUE, EXCHANG_NAME, ROUTING_KEY);

        // 5、创建消费者
        MyConsumer consumer = new MyConsumer(channel);
        channel.basicConsume(QUEUE, true, consumer);
    }
}
