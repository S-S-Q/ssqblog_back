package com.ssq.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssq.config.aspect.Identity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
 *
 * @author SSQ
 * @since 2022-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ssq_blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "对应文件名不能为空")
    private String filename;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "描述不能为空")
    private String description;

    @NotBlank(message = "状态不可为空")
    //表示状态 0表示未登录用户不可看见，1表示登录用户可见
    private Integer status;

    private String tags;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created;

    //修改如下
    //去除user_id content字段
    //添加 tags 以及 filename字段
    public Identity getIdentity(){
        switch (status){
            case 0:
                return Identity.ADMIN;
            case 1:
                return Identity.CONSUMER;
        }

        return Identity.NOT_LOG;
    }
}
