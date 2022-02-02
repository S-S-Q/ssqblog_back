package com.ssq.service;

import com.ssq.pojo.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ssq.pojo.RespBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    RespBean getDetailBlog(Long id, HttpServletRequest request);

    RespBean editBlog(Blog blog);

    RespBean addBlog(MultipartFile file);

    RespBean getAllBlogList();

    RespBean getBlogsByTag(String tagName);

    RespBean deleteBlogsById(List<Long> list);

    RespBean updateBlogStatusById(Long id,Boolean status);

    RespBean searchBlogs(String key);
}
