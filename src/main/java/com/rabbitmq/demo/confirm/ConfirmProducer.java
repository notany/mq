package com.rabbitmq.demo.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

import java.io.IOException;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 15:43
 * @description 消息确认生产者
 **/
public class ConfirmProducer {

    // 队列
    private static final String QUEUE = "test.confirm.queue";
    // 交换机
    private static final String EXCHANG_NAME = "test.confirm.exchang";
    // 路由key
    private static final String ROUTING_KEY = "test.confirm.one";

    public static void main(String args[]) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道、并指定消息确认模式
        Channel channel = connection.createChannel();
        // 消息确认模式
        channel.confirmSelect();
        // 5、发送消息
        String msg = "this is confirm message !";
        channel.basicPublish(EXCHANG_NAME, ROUTING_KEY, null, msg.getBytes());
        // 6、添加消息确认监听
        channel.addConfirmListener(new ConfirmListener() {
            // 消息确认
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println("--------handle Ack---------");
            }

            // 消息未确认
            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("--------handle No Ack---------");
            }
        });
    }

}
