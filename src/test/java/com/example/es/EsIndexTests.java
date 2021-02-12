package com.example.es;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * 关于索引的一些基础操作
 */
@SpringBootTest
class EsIndexTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     */
    @Test
    void createIndex() throws IOException {
        // 1、创建一个索引请求
        CreateIndexRequest request = new CreateIndexRequest("demo");
        // 2、客户端执行请求
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(createIndexResponse));
    }

    /**
     * 判断指定的索引是否存在
     * @throws IOException
     */
    @Test
    void getIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("demo");
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @Test
    void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("demo");
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response));
        System.out.println(response.isAcknowledged());
    }
}
