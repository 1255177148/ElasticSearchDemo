package com.example.es.util;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 解析高亮字段数据，并替换原有内容工具类
 * @Author Zhanzhan
 * @Date 2021/2/12 21:26
 **/
@Component
public class ParseHighlightFieldUtil {

    /**
     * 解析查询结果，并将高亮字段的内容替换掉原来对应字段的内柔
     * @param searchResponse 查询结果
     * @param field 要高亮的字段
     * @return 解析的结果
     */
    public List<Map<String, Object>> parseHighlightField(SearchResponse searchResponse, String field){
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit documentFields : searchResponse.getHits().getHits()){
            /**
             * 解析高亮字段
             * 这里要用高亮字段的数据替代原来的对应字段的数据，实现高亮
             */
            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();// 获取高亮数据
            HighlightField highlightField = highlightFields.get(field);
            Map<String, Object> source = documentFields.getSourceAsMap();// 原来查询的结果
            // 替换
            if (highlightField != null){
                Text[] fragments = highlightField.fragments();
                StringBuilder temp = new StringBuilder();
                for (Text text : fragments){
                    temp.append(text);
                }
                source.put(field, temp.toString()); // 替换掉原来的内容
            }

            list.add(documentFields.getSourceAsMap());
        }
        return list;
    }
}
