package com.rabbitmq.demo.dlx2;

/**
 * @Author: red
 * @Description:
 * @Create: 2022/10/31 19:40
 */

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * 延迟队列生产者
 * http://events.jianshu.io/p/46b649907d53
 */
public class DelayProducer {
    // 普通交换机名称
    private static final String NORMAL_EXCHANGE = "delay_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = MqUtils.getChannel();

        List<Integer> delayedTimes = Arrays.asList(5, 2, 3, 4, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Integer delayedTime : delayedTimes) {
            String content = String.format("消息时间:[%s],延时[%d]s", sdf.format(new Date()), delayedTime);
            byte[] msg = content.getBytes(StandardCharsets.UTF_8);
            Map<String,Object> headers = new HashMap<>();
            headers.put("x-delay",delayedTime * 1000);
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().headers(headers).build();
            channel.basicPublish(NORMAL_EXCHANGE, "key1", properties, msg);
            System.out.println(content);

        }
    }

}