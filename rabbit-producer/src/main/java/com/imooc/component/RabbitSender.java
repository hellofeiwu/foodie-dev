package com.imooc.component;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;
import java.util.UUID;

@Component
public class RabbitSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 这个就是收到broker回执后的监听接口
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback(){

        /**
         * @param correlationData   包含指定业务唯一标识ID
         * @param ack   broker返回的回执
         * @param cause     失败情况下的一些信息
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.out.println("---> 消息ACK结果： " + ack
                                + ", correlationData: "
                                + correlationData.getId());
        }
    };

    /**
     * 对外发送消息的方法
     * @param message   具体的消息内容
     * @param properties    额外的附加属性
     */
    public void send(Object message, Map<String, Object> properties) {
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message<Object> msg = MessageBuilder.createMessage(message, messageHeaders);

        rabbitTemplate.setConfirmCallback(confirmCallback);

        // 指定业务唯一标识ID
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message) throws AmqpException {
                System.out.println("---> In messagePostProcessor: " + message);
                return message;
            }
        };
        rabbitTemplate.convertAndSend("exchange-1",
                                "springboot.rabbit",
                                        msg,
                                        messagePostProcessor,
                                        correlationData);
    }
}
