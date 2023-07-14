package com.rabbitmq.demo.qos1;

/**
 * @Author: red
 * @Description:
 * @Create: 2022/11/1 23:55
 */

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author:luzaichun
 * @Date:2020/12/23
 * @Time:22:18
 **/
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("/");
        factory.setHost("127.0.0.1");
        factory.setPort(5672);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        channel.exchangeDeclare("limit_exchange","topic",false,false,null);
        channel.queueDeclare("limit_queue",false,false,false,null);
        channel.queueBind("limit_queue","limit_exchange","topic.#");

        /**
         *第一个参数:prefetchSize消息的大小限制,0不做限制
         *第二个参数:prefetchCount一次最多推送多少消息，ack完后，再推这么多过来
         * 第三个参数:global 限流策略应用级别。true-通道channel级别限制  false-consumer级别限制
         * */
        channel.basicQos(0,2,false);

        //限流，autoAck一定要设置为false
        channel.basicConsume("limit_queue",false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                System.out.println("消费端接收到消息："+new String(body));
                System.out.println("=======================");
                //prefetchCount=1时候，mq一次给consumer推送1条消息，收到ack后才会继续推送下一条消息
//                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });
    }
}

