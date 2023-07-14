package com.rabbitmq.demo.dlx2;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列
 * 消费者1
 */
public class Consumer01 {
    // 普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    // 死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";
    // 普通队列名称
    private static final String NORMAL_QUEUE = "normal_queue";
    // 死信队列名称
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = MqUtils.getChannel();
        // 声明普通交换机和死信交换机类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 声明普通队列
        Map<String, Object> arguments = new HashMap<>();

        // 正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 设置死信routing key
        arguments.put("x-dead-letter-routing-key", "dead");

        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        // 声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        // 绑定普通的队列和交换机
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "normal");
        // 绑定死信的队列和交换机
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "dead");

        System.out.println("等待接收消息");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer01接收的消息是：" + new String(message.getBody(), "UTF-8"));
        };
        //自动ack
        channel.basicConsume(NORMAL_QUEUE, true,deliverCallback, consumerTag->{});
    }
}
