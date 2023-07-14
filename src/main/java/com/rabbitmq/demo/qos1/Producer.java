package com.rabbitmq.demo.qos1;

/**
 * @Author: red
 * @Description:
 * @Create: 2022/11/1 23:54
 */

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author:luzaichun
 * @Date:2020/12/23
 * @Time:22:10
 **/
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("/");
        factory.setPort(5672);
        factory.setHost("127.0.0.1");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        for (int i = 0; i < 5 ; i++) {
            channel.basicPublish("limit_exchange","topic.limit",true,null,"limit测试".getBytes());
        }
    }
}


