package com.rabbitmq.demo.common;

import com.rabbitmq.client.ConnectionFactory;

/**
 * @author 核桃(wy)
 * @date 2020/8/4 10:50
 * @description
 **/
public class RabbitmqConnectionFactory {

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
}
