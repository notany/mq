package com.rabbitmq.demo.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 17:37
 * @description 生产者
 **/
public class DlxProducer {

    // 普通的队列信息
    private static final String EXCHANG_NAME = "test.dlx.exchang";
    private static final String ROUTING_KEY = "test.dlx.one";

    public static void main(String args[]) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        // 4、发送消息,不需要路由也能投递,消费者1和消费者2都能收到消息
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                // 消息过期时间5s
                .expiration("5000")
                .build();
        String msg = "this is dlx exchang msg !";
        channel.basicPublish(EXCHANG_NAME, ROUTING_KEY, properties, msg.getBytes());
        // 5、关闭
        channel.close();
        connection.close();
    }

}
