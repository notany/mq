package com.rabbitmq.demo.qos10;

/**
 * @Author: red
 * @Description:
 * @Create: 2022/11/2 0:15
 */


import com.rabbitmq.client.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * 推消息
 * qos 保证机制 仅对推消息可用，对拉消无效
 */
public class ListenerConsumer {


    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {

        //连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://guest:guest@localhost:5672/%2f");
//        connectionFactory.setVirtualHost("/");
//        connectionFactory.setUsername("guest");
//        connectionFactory.setPassword("guest");
//        //amqp 协议端口
//        connectionFactory.setPort(5672);

        //建立连接
        Connection connection = connectionFactory.newConnection();
        //获取通道
        Channel channel = connection.createChannel();

        //定义消息队列
        channel.queueDeclare("queue.qos", true, false, false, null);

        //todo 仅对推消息可用
        //表示Qos 是10个消息，最多有10个消息等待确认
        channel.basicQos(10);
        //如果设置为ture 表示使用当前channle的客户端的Consumer 该设置都生效，false表示仅限当前Consumer
//        channel.basicQos(10,false);
        //第一个参数表示未确认消息的大小，mq没有实现，不用管，一般第三个用不到。
//        channel.basicQos(100,10,true);

        //false 表示不自动确认
        channel.basicConsume("queue.qos", false, new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("获取到的消息" + new String(body));


                try {
                    //模拟消费消息时间
                    Thread.sleep(3_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //envelope.getDeliveryTag() deliveryTag表示消息的唯一标志，

                //手动确认消息 第二个参数表示表示是否是批量确认
//                channel.basicAck(envelope.getDeliveryTag(), false);

                //批量确认 减少每个消息都发送去人带来的网络影响
                //如果对于每个消息都发送确认，增加了网络流量，此时可以批量确认消息。如果设置了 multiple为true，消费者在确认的时候，比如说id是8的消息确认了，则在8之前的所有消息都 确认了。
                channel.basicAck(envelope.getDeliveryTag(), true);

                //手动确认 者处理失败
                //第二个参数表示不确认多个还是一个消息，最后一个参数表示不确认的消息是否重新放回队列
                //todo qos保证机制必须确认机制 , 不支持NONE Ack模式。
//                channel.basicNack(envelope.getDeliveryTag(),false,true);

                //手动拒绝消息(只能拒收一条消息)。第二个参数表示是否重新入列，是否重新入列，然后重发会一直重发重试
//                channel.basicReject(envelope.getDeliveryTag(), true);
            }
        });

//        channel.close();
//        connection.close();


    }
}


