package com.imooc.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Sender {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1. 创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        // 2. 创建Connection
        Connection connection = connectionFactory.newConnection();

        // 3. 创建Channel
        Channel channel = connection.createChannel();

        // 4. 注入参数
        String queueName = "test001";
        channel.queueDeclare(queueName, false, false, false, null);

        Map<String, Object> headers = new HashMap<String, Object>();
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .headers(headers)
                .build();

        for (int i=0; i<5; i++) {
            String msg = "Hello World RabbitMQ " + i;
            // 这里第一个参数是exchange，设置为""，但其实rabbitmq会使用 AMQP default 这个exchange
            // 第二个参数是routing key, 在exchange, queue, routing key这三者没有做绑定的情况下，routing key和 queue name要相同
            channel.basicPublish("", queueName, properties, msg.getBytes(StandardCharsets.UTF_8));
        }
    }
}
