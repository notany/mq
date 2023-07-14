package com.rabbitmq.demo.dlx2;

/**
 * @Author: red
 * @Description:
 * @Create: 2022/11/1 0:00
 */

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列生产者
 */
public class ProducerMax {
    // 普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        List<String> list = Arrays.asList("q","w","e","r");
        Collections.rotate(list,2);
//        Channel channel = MqUtils.getChannel();
//        for (int i = 1; i < 101; i++) {
//            String message = "info" + i;
//            channel.basicPublish(NORMAL_EXCHANGE, "normal", null, message.getBytes());
//        }
    }
}
