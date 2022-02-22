package com.ssq.pojo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsBlogDao extends ElasticsearchRepository<EsBlog,Long> {
}
