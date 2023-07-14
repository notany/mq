package com.rabbitmq.demo.dlx2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: red
 * @Description:
 * @Create: 2022/10/31 19:42
 */
public class MqUtils {
    /**
     * 获取一个rabbitmq连接工厂
     * @return
     */
    public static ConnectionFactory getConnectionFactory(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");// 主机
        factory.setPort(5672);// 端口号
        factory.setVirtualHost("/");// 虚拟机
        factory.setUsername("guest");// 用户名
        factory.setPassword("guest");// 密码
        factory.setAutomaticRecoveryEnabled(true);// 是否支持自动重连
        factory.setNetworkRecoveryInterval(3000);// 多久重连一次
        return factory;
    }

    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        // 3、创建通道
        Channel channel = connection.createChannel();
        return channel;
    }
}
