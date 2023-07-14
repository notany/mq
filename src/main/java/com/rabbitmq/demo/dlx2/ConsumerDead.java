package com.rabbitmq.demo.dlx2;

/**
 * @Author: red
 * @Description:
 * @Create: 2022/10/31 19:46
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列
 * 消费者1
 */
public class ConsumerDead {

    // 死信队列名称
    private static final String DEAD_QUEUE = "dead_queue";
    // 死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = MqUtils.getChannel();
        // 绑定死信的队列和交换机，也可以不绑
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "dead");

        System.out.println("等待接收消息");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer02接收的消息是：" + new String(message.getBody(), "UTF-8"));
        };
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> {
        });
    }
}