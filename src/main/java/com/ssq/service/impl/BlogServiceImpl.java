package com.ssq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ssq.pojo.Blog;
import com.ssq.mapper.BlogMapper;
import com.ssq.pojo.RespBean;
import com.ssq.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    BlogMapper blogMapper;

    //获取帖子详情页
    @Override
    public RespBean getBlogList(Integer currentPage) {
        Page page=new Page(currentPage,5);
        IPage pageData=blogMapper.selectPage(page,new QueryWrapper<Blog>().orderByDesc("created"));
        return RespBean.success("获取博客列表成功",pageData);
    }

    @Override
    public RespBean getDetailBlog(Long id) {
        Blog blog=blogMapper.selectById(id);
        if(blog==null)
            return RespBean.error("博客已被删除");
        return RespBean.success("获取博客详情页成功",blog);
    }

    @Override
    public RespBean editBlog(Blog blog) {
        if(blog.getId()!=null
                &&blogMapper.selectById(blog.getId())!=null)
        {
            blogMapper.updateById(blog);
            return RespBean.success("修改成功");
        }
        else
        {
            blogMapper.insert(blog);
        }
        return RespBean.success("添加成功");
    }
}
