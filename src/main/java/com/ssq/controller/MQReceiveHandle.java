package com.ssq.controller;

import com.rabbitmq.client.Channel;
import com.ssq.config.RabbitMQConfig;
import com.ssq.service.IBlogService;
import com.ssq.util.FileUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public class MQReceiveHandle {

    @Autowired
    IBlogService blogService;
    @Value("${file.mdPath}")
    public String mdPath;

    //监听blog队列
    @RabbitListener(queues = {RabbitMQConfig.QUEUE_INFORM_BLOG})
    public void receive_blog(Object msg, Message message, Channel channel){
        String fileName=new String(message.getBody());
        if(FileUtil.existsFile(fileName,mdPath))
            blogService.addBlogInEsAndMySQL(fileName);
    }
}
