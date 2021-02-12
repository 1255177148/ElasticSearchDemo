package com.example.es.controller;

import com.example.es.service.ContentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author Zhanzhan
 * @Date 2021/2/12 16:37
 **/
@RestController
public class ContentController {

    @Resource
    private ContentService contentService;

    @GetMapping("/parse")
    public Boolean parse(@RequestParam("keyword") String keyword) throws IOException {
        return contentService.parseContent(keyword);
    }

    @GetMapping("/search")
    public List<Map<String, Object>> search(@RequestParam("keyword") String keyword,
                                            @RequestParam("pageNo") int pageNo,
                                            @RequestParam("pageSize") int pageSize) throws IOException {
        return contentService.searchPage(keyword, pageNo, pageSize);
    }
}
