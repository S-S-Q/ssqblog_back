package com.ssq;


import com.ssq.pojo.EsBlog;
import com.ssq.pojo.EsBlogDao;
import com.ssq.service.IBlogService;
import com.ssq.service.impl.BlogServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
public class SpringTest {

    @Autowired
    EsBlogDao esBlogDao;
    @Autowired
    IBlogService blogService;

    @Test
    public void addEsBlog()
    {        EsBlog esBlog=new EsBlog();
        esBlog.setTitle("东南大苏打撒境内单");
        esBlog.setCreated(LocalDateTime.now());
        esBlog.setId(2L);
        esBlog.setDescription(" 而且我i的期望哦极目望去机票沃尔玛请问跑去灭却颇为篇");
//        try {
            esBlog.setContent("破我曾经叵测马嵬坡企鹅可前往金额破企鹅去");
//        }catch (IOException e)
//        {
//            System.out.println("文件转换出现问题");
//        }

        esBlogDao.save(esBlog);

    }


    @Test
    public void findById()
    {
        List<EsBlog> esBlogs= (List<EsBlog>) esBlogDao.findAll();
        for(EsBlog esBlog:esBlogs)
        System.out.println(esBlog);
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
