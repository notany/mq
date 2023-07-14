package com.rabbitmq.demo.dlx2;

/**
 * @Author: red
 * @Description:
 * @Create: 2022/10/31 19:40
 */
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列生产者
 */
public class Producer {
    // 普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = MqUtils.getChannel();
        // 设置TTL时间，死信消息，单位毫秒ms，此处10秒(10s未消费会变成进入死信)
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 1; i < 101; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "normal", properties, message.getBytes());
        }
    }
}