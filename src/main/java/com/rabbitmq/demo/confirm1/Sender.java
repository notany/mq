package com.rabbitmq.demo.confirm1;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Sender {
    // RabbitMq 生产者入门示例
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

        // 3、创建通信通道
        Channel channel = connection.createChannel();
        // 4、创建队列，如果队列已经存在，则不会执行任何操作，如果队列不存在，会执行创建队列的操作
        //String queueName = "test-001";
        // 参数：队列名称、是否持久化、是否独占（队列仅供此连接使用，即只能有一个消费者）、不使用时是否自动删除、其他参数
        //channel.queueDeclare("test-001", false, false, false , null);
        // 5、创建消息的属性
        Map<String, Object> headers = new HashMap<>();
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .headers(headers).build();

        // 生产消息
        String exchangeName = "test-confirmListener-exchange";
        String routeKey = "confirm.save";
        // String routeKey = "test.save";
        String messagePrefix = "RabbitMq Message test00";
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("--------------OK-----------------");
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("--------------NOT OK-----------------");
            }
        });
        for(int i = 0; i < 5; i++){
            String message = messagePrefix + i;
            // 生产者端在发送消息时，只需要关心exchange和routeKey即可
            channel.basicPublish(exchangeName, routeKey, properties, message.getBytes());
        }

    }
}
