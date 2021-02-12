package com.example.es.util;

import org.springframework.stereotype.Component;

/**
 * 分页查询工具类
 * @Author Zhanzhan
 * @Date 2021/2/12 17:02
 **/
@Component
public class PageUtil {

    /**
     * 根据页码和页面容量，获取从第几条数据开始查询
     * @param pageNo 页码
     * @param pageSize 页面容量
     * @return 查询位置
     */
    public int getPageFrom(int pageNo, int pageSize){
        if (pageNo <= 1){
            return 0;
        }
        return pageSize * (pageNo - 1);
    }
}
