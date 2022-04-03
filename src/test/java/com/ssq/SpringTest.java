package com.ssq;


import com.ssq.config.RabbitMQConfig;
import com.ssq.pojo.EsBlog;
import com.ssq.pojo.EsBlogDao;
import com.ssq.service.IBlogService;
import com.ssq.service.IVisitorService;
import com.ssq.service.impl.BlogServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
public class SpringTest {

    @Autowired
    EsBlogDao esBlogDao;
    @Autowired
    IBlogService blogService;
    @Autowired
    IVisitorService visitorService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void addEsBlog()
    {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_DIRECT_INFORM, RabbitMQConfig.ROUTINGKEY_BLOG, "SSQ");
    }


    @Test
    public void findById()
    {
        System.out.println(visitorService.getStaticsMsg());;
    }

    @Test
    public void deleteById()
    {
       esBlogDao.deleteAll();
    }

    @Test
    public void searchHighLight()
    {

        List<EsBlog> esBlogList= (List<EsBlog>) blogService.searchBlogs("破我").getData();
        for(EsBlog esBlog:esBlogList)
        {
            System.out.println(esBlog);
        }
        System.out.println(esBlogList.size());
    }

}
