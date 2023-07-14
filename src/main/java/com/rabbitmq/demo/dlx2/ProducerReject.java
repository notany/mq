package com.rabbitmq.demo.dlx2;

/**
 * @Author: red
 * @Description:
 * @Create: 2022/11/1 0:13
 */

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列生产者
 */
public class ProducerReject {
    // 普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = MqUtils.getChannel();
        for (int i = 1; i < 101; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "normal", null, message.getBytes());
        }
    }
}
