package com.rabbitmq.demo.exchang.noexchang;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.client.MyConsumer;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 11:00
 * @description 消费者，没有交换机的情况
 **/
public class MessageConsumer {

    // 队列名称
    private static final String QUEUE = "test.msg.queue";

    public static void main(String args[]) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        // 4、创建队列
        // durable: true，表示持久化
        // exclusive：独占模式，只允许一个 channel 通道连接，可以保证消费顺序性
        // autoDelete：当队列没有任何关联时，自动删除队列
        channel.queueDeclare(QUEUE, true, false, false, null);
        // 5、创建消费者
        MyConsumer consumer = new MyConsumer(channel);
        // 6、设置通道
        // 队列名称，是否自动签收，消费者
        channel.basicConsume(QUEUE, true, consumer);
    }
}
