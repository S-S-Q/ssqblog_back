package com.ssq.service;

import com.ssq.pojo.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ssq.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author SSQ
 * @since 2022-01-18
 */
public interface IBlogService extends IService<Blog> {

    RespBean getBlogList(Integer currentPage);

    RespBean getDetailBlog(Long id);

    RespBean editBlog(Blog blog);
}
