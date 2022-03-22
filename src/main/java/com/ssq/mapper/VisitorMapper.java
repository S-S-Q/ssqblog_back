package com.ssq.mapper;

import com.ssq.pojo.Visitor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssq.pojo.VisitorStatics;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author SSQ
 * @since 2022-02-24
 */
@Mapper
public interface VisitorMapper extends BaseMapper<Visitor> {

    List<VisitorStatics> getIPGroup();
}
