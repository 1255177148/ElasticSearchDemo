package com.example.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.es.entity.User;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @Author Zhanzhan
 * @Date 2020/11/22 17:27
 * 关于文档的基础操作
 */
@SpringBootTest
public class EsDocumentTest {
    
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 测试添加文档
     */
    @Test
    void addDocument() throws IOException {
        // 创建对象
        User user = new User();
        user.setName("法外狂徒张三");
        user.setAge(3);
        // 创建请求
        IndexRequest request = new IndexRequest("demo");
        // 设置文档id
        request.id("1");
        // 设置超时时间
        request.timeout(TimeValue.timeValueSeconds(1));
        // 将数据放入请求中
        request.source(JSON.toJSONString(user), XContentType.JSON);
        // 客户端发送请求
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response));
    }

    /**
     * 判断文档是否存在
     */
    @Test
    void existsDocument() throws IOException {
        GetRequest request = new GetRequest("demo", "1");
//        request.fetchSourceContext(new FetchSourceContext(false));
//        request.storedFields("name");
        boolean exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 获取文档数据
     * @throws IOException
     */
    @Test
    void getDocument() throws IOException {
        GetRequest request = new GetRequest("demo", "1");
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        String result = response.getSourceAsString();
        User user = JSONObject.parseObject(result, User.class);
        System.out.println(response.toString());
        System.out.println(JSON.toJSONString(user));
    }
}
