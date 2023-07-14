package com.rabbitmq.demo.exchang.headers;

import com.rabbitmq.client.*;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 15:20
 * @description 头交换机生产者
 **/
public class HeadersProducer {

    // 交换机
    public static final String EXCHANG_NAME = "test.header.exchang";

    public static void main(String args[]) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        // 4、发送消息,这里的头信息
        Map<String, Object> headers = new HashMap<>();
        // 这个属性要根据消费者设置的x-match的属性才能投递到
        // 消费者如果设置x-match=all,那么这里的key要和消费者设置的可以完全匹配，才能投递到
        // 消费者如果设置x-match=any,那么这里的可以和消费者设置的其中一个key匹配就能投递到
        headers.put("name", "zhangsan");
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().headers(headers).build();
        String msg = "this is headers exchang msg !";
        channel.basicPublish(EXCHANG_NAME, "", properties, msg.getBytes());
        // 5、关闭
        channel.close();
        connection.close();
    }
}
