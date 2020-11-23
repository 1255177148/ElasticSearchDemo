package com.example.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.es.entity.User;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     *
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

    /**
     * 更新文档数据
     *
     * @throws IOException
     */
    @Test
    void updateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest("demo", "1");
        request.timeout("1s");
        User user = new User();
        user.setName("李四");
        user.setAge(3);
        request.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response));
    }

    /**
     * 删除文档数据
     *
     * @throws IOException
     */
    @Test
    void deleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("demo", "1");
        request.timeout("1s");
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response));
    }

    /**
     * 批量插入文档数据
     *
     * @throws IOException
     */
    @Test
    void batchAddDocument() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");
        List<User> users = new ArrayList<>();
        users.add(new User("1", 1));
        users.add(new User("2", 2));
        users.add(new User("3", 3));
        users.add(new User("4", 4));
        for (int i = 0; i < users.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("demo")
                            .id("" + (i + 1))
                            .source(JSON.toJSONString(users.get(i)), XContentType.JSON)
            );
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(bulk));
        System.out.println(bulk.hasFailures());
    }

    /**
     * 带条件查询
     * @throws IOException
     */
    @Test
    void search() throws IOException {
        SearchRequest searchRequest = new SearchRequest("demo");
        // 构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 创建查询构建器

        // 类似sql语句的  where name = 1 and name = 2
        BoolQueryBuilder name = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("name", "1"))
                .must(QueryBuilders.matchQuery("name", "2"));

        // 类似sql语句的 where name = 1 or name = 2
        BoolQueryBuilder name2 = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("name", "1"))
                .should(QueryBuilders.matchQuery("name", "2"));

        // 类似sql语句的 where name != 1 and name != 2
        BoolQueryBuilder name3 = QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.matchQuery("name", "1"))
                .mustNot(QueryBuilders.matchQuery("name", "2"));

        searchSourceBuilder.query(name2);
        // 构建高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<p class='key' style='color:red'>");
        highlightBuilder.postTags("</p>");
        highlightBuilder.field("name");
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response));
        System.out.println(Arrays.toString(response.getHits().getHits()));
    }
}
