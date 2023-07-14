package com.rabbitmq.demo.dlx1;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 生产者
 * 死信队列使用
 */
public class Producer {

    public static void main(String[] args) throws Exception{

        //得到连接
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        //创建通道
        Channel channel=connection.createChannel();
        String exchangeName = "order_exchange";
        String routingKey = "order.save";

        //通过在properties设置来标识消息的相关属性
        for(int i=0;i<5;i++){
            Map<String, Object> headers = new HashMap<String, Object>();
            headers.put("num",i);
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                    .deliveryMode(2)                    // 传送方式 2:持久化投递
                    .contentEncoding("UTF-8")           // 编码方式
                    //.expiration("10000")              // 过期时间
                    .headers(headers)                  //自定义属性
                    .build();
            String message = "hello this is ack message ....."  + i;
            System.out.println(message);
            channel.basicPublish(exchangeName,routingKey,true,properties,message.getBytes());
        }

    }
}