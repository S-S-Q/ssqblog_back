package com.ssq.service.impl;

import com.ssq.pojo.Blog;
import com.ssq.mapper.BlogMapper;
import com.ssq.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author SSQ
 * @since 2022-01-18
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

}
