package com.ssq.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author SSQ
 * @since 2022-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ssq_visitor")
public class Visitor implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate visit_Time;


}
