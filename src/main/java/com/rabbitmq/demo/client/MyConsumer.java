package com.rabbitmq.demo.client;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 10:54
 * @description 自定义消费者
 **/
public class MyConsumer extends DefaultConsumer {

    private Channel channel;

    private static Boolean isCheckOut = false;

    private Hashtable<String, Long> consumed = new Hashtable<>();

    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    /**
     * 重写父类方法
     *
     * @param consumerTag
     * @param envelope
     * @param properties
     * @param body
     */
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("--------------- MyConsumer Message ----------------");
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "        " + new String(body));
        try {
            if (!isCheckOut) {
                checkoutTimeoutMessageId();
            }
            Thread.sleep(500);
//            int a = 1 / 0;
            // 手动确认消息,false：不批量签收
            channel.basicAck(envelope.getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
//            // 未签收，第三参数：是否重回队列
//            channel.basicNack(envelope.getDeliveryTag(), false, true);
        }
    }

    private void checkoutTimeoutMessageId() {
        isCheckOut = true;
        Set<String> keys = new HashSet<>();
        for (Map.Entry<String, Long> entry : consumed.entrySet()) {
            if (entry.getValue()<System.currentTimeMillis()){
                keys.add(entry.getKey());
            }
        }
        for(String key:keys){
            consumed.remove(key);
        }
        isCheckOut = false;
    }
}
