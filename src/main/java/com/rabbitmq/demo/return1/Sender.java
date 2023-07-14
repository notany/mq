package com.rabbitmq.demo.return1;

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
        String exchangeName = "test-returnListener-exchange";
        String routeKey = "test.save";
        // String routeKey = "test.save";
        String messagePrefix = "RabbitMq Message test00";
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("return relyCode: " + replyCode);    // 状态码
                System.out.println("return replyText: " + replyText);   // 结果信息
                System.out.println("return exchange: " + exchange);
                System.out.println("return routingKey: " + routingKey);
                System.out.println("return properties: " + properties);
                System.out.println("return body: " + new String(body));

            }
        });
        boolean mandatory = true;
        for(int i = 0; i < 5; i++){
            String message = messagePrefix + i;
            // 生产者端在发送消息时，只需要关心exchange和routeKey即可
            channel.basicPublish(exchangeName, routeKey, mandatory, properties, message.getBytes());
        }

    }
}
