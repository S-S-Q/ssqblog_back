package com.ssq.config;

import com.ssq.pojo.Blog;
import org.elasticsearch.repositories.Repository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BlogRepository extends ElasticsearchRepository<Blog,Long> {
}
