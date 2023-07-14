package com.rabbitmq.demo.exchang.noexchang;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 11:08
 * @description 生产者，没有交换机的情况生产消息
 **/
public class MessageProducer {

    // 定义路由key
    private static final String ROUTING_KEY = "test.msg.queue";

    public static void main(String args[]) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        // 4、设置消息属性
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)// 1:非持久化，2：持久化
                .contentEncoding("UTF-8")// 字符集
                .expiration("10000")// 过期时间
                .build();
        // 5、发送消息
        // 第一个参数为交换机，如果没有指定则使用rabbitmq默认的交换机，此时路由key要和队列名称完全详情才能发送到
        channel.basicPublish("", ROUTING_KEY, properties, "this is mq msg!".getBytes());
        // 6、关闭连接和通道
        channel.close();
        connection.close();
    }
}
