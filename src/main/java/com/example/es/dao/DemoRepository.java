package com.example.es.dao;

import com.example.es.entity.Demo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author elvis
 * @Date 2022/4/25 10:14
 */
public interface DemoRepository extends ElasticsearchRepository<Demo, String> {
}
