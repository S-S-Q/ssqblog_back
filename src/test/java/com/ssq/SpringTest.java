package com.ssq;


import com.ssq.pojo.EsBlog;
import com.ssq.pojo.EsBlogDao;
import com.ssq.service.IBlogService;
import com.ssq.service.IVisitorService;
import com.ssq.service.impl.BlogServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    public void addEsBlog()
    {
        System.out.println(redisTemplate.opsForValue().get("blog_comment::10_1"));

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
