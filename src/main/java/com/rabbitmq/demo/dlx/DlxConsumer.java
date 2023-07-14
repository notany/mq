package com.rabbitmq.demo.dlx;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.client.MyConsumer;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 17:37
 * @description 死信机制
 **/
public class DlxConsumer {

    // 普通的队列信息
    private static final String QUEUE = "test.dlx.queue";
    private static final String EXCHANG_NAME = "test.dlx.exchang";
    private static final String ROUTING_KEY = "test.dlx.#";

    // 死信队列信息
    private static final String QUEUE_DLX = "dlx.queue";
    private static final String EXCHANG_NAME_DLX = "dlx.exchang";
    private static final String ROUTING_KEY_DLX = "#";

    public static void main(String[] args) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        // 4、普通队列声明
        channel.exchangeDeclare(EXCHANG_NAME, BuiltinExchangeType.TOPIC, true, false, false, null);
        // 创建队列的时候要声明死信交换机的名称
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", EXCHANG_NAME_DLX);
        channel.queueDeclare(QUEUE, true, false, false, params);
        channel.queueBind(QUEUE, EXCHANG_NAME, ROUTING_KEY);

        // 5、死信队列声明
        channel.exchangeDeclare(EXCHANG_NAME_DLX, BuiltinExchangeType.TOPIC, true, false, false, null);
        channel.queueDeclare(QUEUE_DLX, true, false, false, null);
        channel.queueBind(QUEUE_DLX, EXCHANG_NAME_DLX, ROUTING_KEY_DLX);

        // 6、创建消费者
        MyConsumer consumer = new MyConsumer(channel);
        channel.basicConsume(QUEUE, true, consumer);

    }
}
