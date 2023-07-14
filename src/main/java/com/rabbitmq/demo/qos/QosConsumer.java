package com.rabbitmq.demo.qos;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.client.MyConsumer;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 17:27
 * @description
 **/
public class QosConsumer {

    private static final String QUEUE = "test.qos.queue";
    private static final String EXCHANG_NAME = "test.qos.exchang";
    private static final String ROUTING_KEY = "test.qos.*";

    public static void main(String args[]) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        // 4、创建交换机、队列并绑定
        channel.exchangeDeclare(EXCHANG_NAME, BuiltinExchangeType.TOPIC, true, false, false, null);
        channel.queueDeclare(QUEUE, true, false, false, null);
        channel.queueBind(QUEUE, EXCHANG_NAME, ROUTING_KEY);
        // 5、创建消费者
        MyConsumer consumer = new MyConsumer(channel);
        // 使用qos,然后必须手动签收
        // 在非自动签收的情况下，在一定数量的消息未确认前，不消费新的消息
        channel.basicQos(0, 1, false);
        channel.basicConsume(QUEUE, false, consumer);
    }
}
