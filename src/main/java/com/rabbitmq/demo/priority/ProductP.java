package com.rabbitmq.demo.priority;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Author: red
 * @Description:
 * @Create: 2023/1/15 12:58
 */
public class ProductP {
    private static final String QUEUE_NAME="hello66";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("/");
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //给消息赋予一个priority属性
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
        for(int i=1;i<11;i++){
            String message = "info"+i;
            if(i==5){
                channel.basicPublish("",QUEUE_NAME,properties,message.getBytes());
            }else {
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            }
        }
    }
}

