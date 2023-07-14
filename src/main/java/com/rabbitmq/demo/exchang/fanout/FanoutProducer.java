package com.rabbitmq.demo.exchang.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 14:27
 * @description 生产者
 **/
public class FanoutProducer {

    // 交换机
    private static final String EXCHANG_NAME = "test.fanout.exchang";

    public static void main(String args[]) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        // 4、发送消息,不需要路由也能投递,消费者1和消费者2都能收到消息
        String msg = "this is fanout exchang msg !";
        channel.basicPublish(EXCHANG_NAME, "", null, msg.getBytes());
        // 5、关闭
        channel.close();
        connection.close();
    }
}
