package com.rabbitmq.demo.confirm1;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receiver {
    // RabbitMq 消费者示例
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1、创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        // 2、生产连接
        Connection connection = connectionFactory.newConnection();

        // 3、生产通信通道
        Channel channel = connection.createChannel();
        String exchangeName = "test-confirmListener-exchange";
        String queueName = "test-confirmListener-queue";
        String routeKey = "confirm.#";
        // 4、创建队列，如果队列已经存在，则不会执行任何操作，如果队列不存在，会执行创建队列的操作
        channel.exchangeDeclare(exchangeName, "topic", true, false, false, null);
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, exchangeName, routeKey);
        //创建队列消费者
        DefaultConsumer consumer = new DefaultConsumer(channel){
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.err.println("接收消息: " + new String(body, "utf-8"));
            }
        };
        // 参数介绍：队列名称、是否自动Ack、消费者
        channel.basicConsume(queueName, true, consumer);
    }
}
