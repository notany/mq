package com.rabbitmq.demo.qos10;

/**
 * @Author: red
 * @Description:
 * @Create: 2022/11/2 0:15
 */

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * 生产者生产消息
 */
public class Produce {


    public static void main(String[] args) throws IOException, TimeoutException {


        //连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        //amqp 协议端口
        connectionFactory.setPort(5672);

        //简历连接
        Connection connection = connectionFactory.newConnection();

        //获取通道
        Channel channel = connection.createChannel();

        //声明队列
        channel.queueDeclare("queue.qos", true, false, false, null);

        //声明交换器  交换器名称, 交换器类型，是否持久化，是否自动删除的， 属性map几乎
        channel.exchangeDeclare("ex.qos", BuiltinExchangeType.FANOUT, false, false, false, null);

        //发送消息  交换器 路由key,属性，消息 -> amqp协议会将消息发送出去
        channel.queueBind("queue.qos", "ex.qos", "qos.key");

        String aeration = "hello-";
        for (int i = 0; i < 20; i++) {
            channel.basicPublish("ex.qos", "qos.key", null, (aeration + " --- > " + i).getBytes());

        }

        channel.close();
        connection.close();


    }
}


