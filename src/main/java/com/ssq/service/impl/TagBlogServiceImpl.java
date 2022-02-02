package com.ssq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssq.pojo.Tag;
import com.ssq.pojo.TagBlog;
import com.ssq.mapper.TagBlogMapper;
import com.ssq.service.ITagBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ssq.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author SSQ
 * @since 2022-01-25
 */
@Service
public class TagBlogServiceImpl extends ServiceImpl<TagBlogMapper, TagBlog> implements ITagBlogService {
    @Autowired
    TagBlogMapper tagBlogMapper;
    @Autowired
    ITagService tagService;



    //删除blogId相关
    @Override
    public void deleteByBlogId(Long id) {

        List<TagBlog> tagBlogList=tagBlogMapper.selectList(new QueryWrapper<TagBlog>().eq("blog_id",id));
        for(TagBlog tagBlog:tagBlogList)
        {
            tagService.decreaseTagNumber(tagBlog.getTag_Id());
            tagBlogMapper.deleteById(tagBlog.getId());
        }

    }

    @Override
    public void addTagBlog(List tagNameList, Long blogId) {
        if(tagNameList==null)
            return;
        for(Object obj: tagNameList)
        {
            int tagId= tagService.addTags((String) obj);
            TagBlog tagBlog=new TagBlog();
            tagBlog.setBlog_Id(blogId);
            tagBlog.setTag_Id(tagId);
            tagBlogMapper.insert(tagBlog);
        }
    }

    @Override
    public List<Long> getBlogIdsByTag(String tagName) {
        int tagId=tagService.getIdByTagName(tagName);
        List <TagBlog> tagBlogList=tagBlogMapper.selectList(new QueryWrapper<TagBlog>().eq("tag_id",tagId));

        List<Long> blogIds=new LinkedList<>();
        for(TagBlog tagBlog: tagBlogList)
        {

            blogIds.add(tagBlog.getBlog_Id());
        }
        return blogIds;
    }

    @Override
    public List<String> getTagNamesByBlogId(Long id) {
        List<TagBlog> tagBlogList=tagBlogMapper.selectList(new QueryWrapper<TagBlog>().eq("blog_id",id));

        List<String> tagNames=new LinkedList<>();
        for(TagBlog tagBlog: tagBlogList)
        {
            Tag tag=tagService.getById(tagBlog.getTag_Id());
            if(tag!=null)
            tagNames.add(tag.getName());
        }
        return tagNames;
    }
}
