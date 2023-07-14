package com.rabbitmq.demo.ack.multiple;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author: red
 * @Description: Total代表队列中的消息总条数，Ready代表消费者还可以读到的条数，Unacked:代表还有多少条没有被应答
 * @Create: 2023/1/11 0:26
 */
public class Consumer2Reject {
    @Test
    public void testBasicConsumer1() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("/");
        factory.setHost("127.0.0.1");
        factory.setPort(5672);    // 5672
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        String EXCHANGE_NAME = "exchange.direct";
        String QUEUE_NAME = "queue_name";
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "key");

//        GetResponse response = channel.basicGet(QUEUE_NAME, false);
//        byte[] body = response.getBody();
//        System.out.println(new String(body).toString());

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(message);
                if (message.contains(":3")){
                    // requeue：重新入队列，false：直接丢弃，相当于告诉队列可以直接删除掉
                    channel.basicReject(envelope.getDeliveryTag(), true);
                } else {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }

            }
        };

        channel.basicConsume(QUEUE_NAME, false, consumer);

        Thread.sleep(10000);
    }
}