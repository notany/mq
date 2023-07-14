package com.rabbitmq.demo.dlx2;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 延迟队列
 */
public class DelayConsumer {
    // 普通交换机名称
    private static final String NORMAL_EXCHANGE = "delay_exchange";
    // 普通队列名称
    private static final String NORMAL_QUEUE = "delay_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = MqUtils.getChannel();


        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct");
        channel.exchangeDeclare(NORMAL_EXCHANGE,"x-delayed-message",true,false,arguments);
        channel.queueDeclare(NORMAL_QUEUE,true,false,false,new HashMap<>());
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"key1");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            System.out.println(sdf.format(new Date()) +"-"+ new String(message.getBody(), "UTF-8"));
        };
        //手动动ack
        channel.basicConsume(NORMAL_QUEUE, true,deliverCallback, consumerTag->{});
    }
}
