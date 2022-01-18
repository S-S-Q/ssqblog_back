package com.ssq.mapper;

import com.ssq.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author SSQ
 * @since 2022-01-18
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
