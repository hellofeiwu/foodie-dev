package com.imooc.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receiver {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 1. 创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        // 2. 创建Connection
        Connection connection = connectionFactory.newConnection();

        // 3. 创建Channel
        Channel channel = connection.createChannel();

        // 4. 注入参数
        String queueName = "test001";
        channel.queueDeclare(queueName, false, false, false, null);

        QueueingConsumer consumer = new QueueingConsumer(channel);

        // 这里的true设置是自动发送ACK
        channel.basicConsume(queueName, true, consumer);

        // 循环获取消息
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery(); // 如果没有消息 这一步会一直阻塞
            String msg = new String(delivery.getBody());
            System.out.println("收到消息：" + msg);
        }
    }
}
