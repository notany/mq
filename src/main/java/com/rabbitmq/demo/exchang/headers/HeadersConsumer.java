package com.rabbitmq.demo.exchang.headers;

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
 * @date 2020/8/4 15:15
 * @description 头交换机消费者
 **/
public class HeadersConsumer {

    // 队列
    public static final String QUEUE = "test.header.queue";
    // 交换机
    public static final String EXCHANG_NAME = "test.header.exchang";

    public static void main(String args[]) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        // 4、创建队列、交换机、绑定
        channel.exchangeDeclare(EXCHANG_NAME, BuiltinExchangeType.HEADERS, true);
        channel.queueDeclare(QUEUE, true, false,false, null);
        // x-match属性：
        // all:一个传送消息的header里的键值对和交换机的header键值对全部匹配，才可以路由到对应交换机
        // any:一个传送消息的header里的键值对和交换机的header键值对任意一个匹配，就可以路由到对应交换机
        Map<String, Object> headers = new HashMap<>();
        headers.put("name", "zhangsan");// 消费者设置了name属性
        headers.put("x-match", "all");
        channel.queueBind(QUEUE, EXCHANG_NAME, "", headers);
        // 5、创建消费者
        MyConsumer consumer = new MyConsumer(channel);
        channel.basicConsume(QUEUE, true, consumer);
    }
}
