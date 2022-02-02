package com.ssq.service;

import com.ssq.pojo.Tag;
import com.ssq.pojo.TagBlog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author SSQ
 * @since 2022-01-25
 */
public interface ITagBlogService extends IService<TagBlog> {

    void deleteByBlogId(Long id);

    void addTagBlog(List tagNameList, Long blogId);

    List<Long> getBlogIdsByTag(String tagName);

    List<String> getTagNamesByBlogId(Long id);
}
