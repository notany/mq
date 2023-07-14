package com.rabbitmq.demo.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 17:14
 * @description 生产者
 **/
public class AckProducer {

    private static final String QUEUE = "test.ack.queue";
    private static final String EXCHANG_NAME = "test.ack.exchang";
    private static final String ROUTING_KEY = "90";

    public static void main(String args[]) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        // 4、发送消息
        String msg = "kzzi is ack msg !";
        for (int i = 0; i < 10; i++) {
            channel.basicPublish(EXCHANG_NAME, ROUTING_KEY, null, (msg + i).getBytes());
        }
        channel.close();
        connection.close();
    }
}
