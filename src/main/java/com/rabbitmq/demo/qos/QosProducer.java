package com.rabbitmq.demo.qos;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 17:32
 * @description
 **/
public class QosProducer {

    private static final String EXCHANG_NAME = "test.qos.exchang";
    private static final String ROUTING_KEY = "test.qos.one";

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
        for(int i = 0; i< 10; i++){
            String msg = "this is confirm message !,i:" +i;
            channel.basicPublish(EXCHANG_NAME, ROUTING_KEY, null, msg.getBytes());
        }
    }
}
