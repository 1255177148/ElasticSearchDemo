package com.example.es.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author Zhanzhan
 * @Date 2021/2/12 16:22
 **/
public interface ContentService {

    /**
     * 解析通过爬虫爬取的京东数据，并放入es中
     * @param keyword 关键字
     * @return
     */
    boolean parseContent(String keyword) throws IOException;

    /**
     * 根据关键字分页查询，并且高亮关键字
     * @param keyword 关键字
     * @param pageNo 页码
     * @param pageSize 每页容量
     * @return
     */
    List<Map<String, Object>> searchPage(String keyword, int pageNo, int pageSize) throws IOException;
}
