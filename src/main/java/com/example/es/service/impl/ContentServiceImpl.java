package com.example.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.es.entity.Content;
import com.example.es.service.ContentService;
import com.example.es.util.PageUtil;
import com.example.es.util.ParseHighlightFieldUtil;
import com.example.es.util.reptile.HTMLParsing;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author Zhanzhan
 * @Date 2021/2/12 16:22
 **/
@Service
public class ContentServiceImpl implements ContentService {

    private final static String INDEX = "jd";

    @Resource
    private HTMLParsing htmlParsing;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private PageUtil pageUtil;

    @Resource
    private ParseHighlightFieldUtil parseHighlightFieldUtil;

    @Override
    public boolean parseContent(String keyword) throws IOException {
        List<Content> contents = htmlParsing.parseJD(keyword);
        // 把爬取到的数据放入 es 中
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(5));
        for (Content content : contents) {
            bulkRequest.add(new IndexRequest(INDEX).source(JSON.toJSONString(content), XContentType.JSON));
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulkResponse.hasFailures();
    }

    @Override
    public List<Map<String, Object>> searchPage(String keyword, int pageNo, int pageSize) throws IOException {
        if (pageNo <= 1){
            pageNo = 1;
        }
        // 条件搜索
        SearchRequest searchRequest = new SearchRequest(INDEX);
        // 构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 分页
        searchSourceBuilder.from(pageUtil.getPageFrom(pageNo, pageSize));
        searchSourceBuilder.size(pageSize);
        // 精确匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
        searchSourceBuilder.query(termQueryBuilder);
        // 高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        // 设置过期时间
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(2));
        // 执行搜索
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        return parseHighlightFieldUtil.parseHighlightField(searchResponse, "title");
    }
}
