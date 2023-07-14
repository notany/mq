package com.rabbitmq.demo.exchang.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 14:09
 * @description 生产者
 **/
public class DirectProducer {

    // 交换机
    private static final String EXCHANG_NAME = "test.direct.exchang";
    // 这个路由要和队列绑定的路由完全相同
    private static final String ROUTING_KEY = "test.direct";

    public static void main(String args[]) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建 通道
        Channel channel = connection.createChannel();
        // 4、发送消息到交换机，交换机根据路由规则投递到队列
        String msg = "this is direct exchang msg!";
        channel.basicPublish(EXCHANG_NAME, ROUTING_KEY, null, msg.getBytes());
        // 5、关闭
        channel.close();
        connection.close();
    }
}
