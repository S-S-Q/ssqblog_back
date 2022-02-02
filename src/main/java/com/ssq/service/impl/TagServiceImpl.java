package com.ssq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssq.pojo.RespBean;
import com.ssq.pojo.Tag;
import com.ssq.mapper.TagMapper;
import com.ssq.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Autowired
    TagMapper tagMapper;

    @Override
    public int addTags(String tagName) {
            //查询是否有存在的 tag
            Tag tag=tagMapper.selectOne(new QueryWrapper<Tag>().eq("name",tagName));
            //不存在的话
            if(tag==null)
            {
                tag=new Tag();
                tag.setName(tagName);
                tag.setNumber(1);
                tagMapper.insert(tag);
            }
            //存在的话
            else {
                tag.setNumber(tag.getNumber() + 1);
                tagMapper.updateById(tag);
            }
            return tag.getId();
    }

    @Override
    public void decreaseTagNumber(Integer id) {
        Tag tag=tagMapper.selectById(id);
        //如果数量少于等于1则删除
        if(tag.getNumber()<=1)
        {
            tagMapper.deleteById(tag);
        }
        //如果数量多于1 则减1
        else
        {
            tag.setNumber(tag.getNumber()-1);
            tagMapper.updateById(tag);
        }
    }

    @Override
    public RespBean getTagList() {
        List<Tag> tags=tagMapper.selectList(new QueryWrapper<Tag>());
        return RespBean.success("获取标签列表成功",tags);
    }

    @Override
    public Integer getIdByTagName(String tagName) {
        Tag tag=tagMapper.selectOne(new QueryWrapper<Tag>().eq("name",tagName));
        return tag.getId();
    }
}
