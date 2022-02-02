package com.ssq.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.elasticsearch.common.text.Text;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsBlog {
    private Long id;
    private String title;
    private Text content;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created;
}
