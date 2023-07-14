package com.rabbitmq.demo.exchang.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.client.MyConsumer;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 14:55
 * @description 消费者
 **/
public class TopicConsumer {

    // 队列
    private static final String QUEUE = "test.topic.queue";
    // 交换机
    private static final String EXCHANG_NAME = "test.topic.exchang";
    // 路由规则 .* 可以匹配一个单词， .#可以匹配多个单词
    //private static final String ROUTING_KEY = "test.topic.*";
//    private static final String ROUTING_KEY = "test.topic.#";
    private static final String ROUTING_KEY = "";

    public static void main(String args[]) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        // 4、创建交换机、队列、路由并绑定
        channel.exchangeDeclare(EXCHANG_NAME, BuiltinExchangeType.TOPIC, true, false, false, null);
        channel.queueDeclare(QUEUE, true, false, false, null);
        channel.queueBind(QUEUE, EXCHANG_NAME, ROUTING_KEY);
        // 5、创建消费者
        MyConsumer consumer = new MyConsumer(channel);
        // autoAck 是否自动签收
        channel.basicConsume(QUEUE, true, consumer);
    }
}
