package com.example.es.util.reptile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 爬虫数据封装类
 * <p>@NoArgsConstructor是无参构造方法注解，可以生成一个无参构造</p>
 * <p>@AllArgsConstructor，此注解可以生成一个所有参数的有参构造方法</p>
 * @Author Zhanzhan
 * @Date 2021/2/8 20:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    private String title;
    private String img;
    private String price;
}
