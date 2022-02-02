package com.ssq.service;

import com.ssq.pojo.RespBean;
import com.ssq.pojo.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author SSQ
 * @since 2022-01-25
 */
public interface ITagService extends IService<Tag> {
    int addTags(String tagName);

    void decreaseTagNumber(Integer id);

    RespBean getTagList();

    Integer getIdByTagName(String tagName);
}
