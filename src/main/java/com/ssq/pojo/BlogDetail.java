package com.ssq.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDetail {
    //渲染的html内容
    private String html;

    //标题
    private String title;

    //标签
    private List<String> tagsName;

    //
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created;
}
