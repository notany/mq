package com.rabbitmq.demo.priority;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: red
 * @Description: 消费者需要等待消息已经发送到队列中才去消费因为，这样才有机会对消息进行排序，所有要先启动生产者，在启动消费者
 *               直接开启消费这监听，直接消费了， 不会有优先的出现
 *               https://www.tinstu.com/2070.html
 * @Create: 2023/1/15 13:01
 */
public class Consumer1 {
    private static final String QUEUE_NAME="hello66";

    public static void main(String[] args) throws Exception {
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        //设置队列的最大优先级 最大可以设置到255，官网推荐1-10，调高吃内存和CPU
        Map<String,Object> params = new HashMap<>();
        params.put("x-max-priority",10);
        channel.queueDeclare(QUEUE_NAME,true,false,false,params);

        System.out.println("消费者启动等待消费..............");
        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            String receivedMessage = new String(delivery.getBody());
            System.out.println("接收到消息:"+receivedMessage);
        };
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,(consumerTag)->{
            System.out.println("消费者无法消费消息时调用，如队列被删除");
        });
    }
}

