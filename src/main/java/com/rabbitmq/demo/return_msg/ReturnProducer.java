package com.rabbitmq.demo.return_msg;

import com.rabbitmq.client.*;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

import java.io.IOException;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 16:58
 * @description 生产者
 **/
public class ReturnProducer {

    private static final String QUEUE = "test.return.queue";
    private static final String EXCHANG_NAME = "test.return.exchang";
    private static final String ROUTING_KEY = "test.return.aa.bb";

    public static void main(String args[]) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        // 设置消息确认模式
        channel.confirmSelect();
        // 添加return监听
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int i, String s, String s1, String s2, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                System.err.println("消息投递失败！！！");
                System.out.println("i:" +i);
                System.out.println("s:" +s);
                System.out.println("s1:" +s1);
                System.out.println("s2:" +s2);
                System.out.println("basicProperties:" +basicProperties);
                System.out.println("body:" +new String(bytes));
            }
        });
        // 4、发送消息
        String msg = "this is return msg !";
        //  mandatory， 设置为true，则监听器会接收到路由不可达的消息， 然后进行处理，如果设置为false，那么broker端自动删除该消息。
        channel.basicPublish(EXCHANG_NAME, ROUTING_KEY, true,null, msg.getBytes());

    }
}
