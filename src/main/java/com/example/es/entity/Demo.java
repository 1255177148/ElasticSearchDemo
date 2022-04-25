package com.example.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author elvis
 * @Date 2022/4/25 14:25
 */
@Document(indexName = "demo_repository")
@Data
public class Demo {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Integer)
    private int age;

    @Field(type = FieldType.Text)
    private String desc;
}
