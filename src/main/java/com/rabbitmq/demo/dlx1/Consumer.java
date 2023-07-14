package com.rabbitmq.demo.dlx1;


import com.rabbitmq.client.*;
import com.rabbitmq.demo.common.RabbitmqConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Consumer {

    public static void main(String[] args) throws Exception{

        //得到连接
        // 1、获取连接工厂
        ConnectionFactory connectionFactory = RabbitmqConnectionFactory.getConnectionFactory();
        // 2、创建连接
        Connection connection = connectionFactory.newConnection();
        //创建通道
        final Channel channel=connection.createChannel();
        //业务交换机和业务队列
        String exchangeName = "order_exchange";
        String exchangeType="topic";
        final String queueName = "order_queue";
        String routingKey = "order.#";

        //死信队列配置  ----------------
        String deadExchangeName = "dlx.exchange";
        String deadQueueName = "dlx.queue";
        String deadRoutingKey = "#";
        //死信队列配置  ----------------

        //将死信消息路由
        Map<String,Object> arguments = new HashMap<String, Object>();
        arguments.put("x-dead-letter-exchange",deadExchangeName);
        arguments.put("x-message-ttl",10000);

        //创建业务交换机
        channel.exchangeDeclare(exchangeName,exchangeType,true,false,false,null);
        //创建业务队列，设置死信交换机
        channel.queueDeclare(queueName,false,false,false,arguments);
        //通过routing key绑定队列和交换机
        channel.queueBind(queueName,exchangeName,routingKey);

        //定义死信交换机
        channel.exchangeDeclare(deadExchangeName,exchangeType,true,false,false,null);
        //定义死信队列
        channel.queueDeclare(deadQueueName,true,false,false,null);
        //绑定交换机和队列
        channel.queueBind(deadQueueName,deadExchangeName,deadRoutingKey);

        System.out.println("consumer启动 .....");

        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try{
                    Thread.sleep(2000);
                }catch (Exception e){

                }
                Integer num = (Integer)properties.getHeaders().get("num");
                //通过消费者回调，根据判断进行消息确认或者拒绝
                if(num==0){
                    //未被ack的消息，并且requeue=false。即nack的 消息不再被退回队列而成为死信队列
                    channel.basicNack(envelope.getDeliveryTag(),false,false);
                    String message = new String(body, "UTF-8");
                    System.out.println(" consumer端的Nack消息是： " + message);
                }else {
                    channel.basicAck(envelope.getDeliveryTag(),false);
                    String message = new String(body, "UTF-8");
                    System.out.println(" consumer端的ack消息是： " + message);
                }
            }
        };
        //消息要能重回队列，需要设置autoAck的属性为false，即在回调函数中进行手动签收
        channel.basicConsume(queueName,false,consumer);
    }
}
