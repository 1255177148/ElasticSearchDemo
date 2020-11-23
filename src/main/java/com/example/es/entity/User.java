package com.example.es.entity;

import lombok.Data;

/**
 * @Author Zhanzhan
 * @Date 2020/11/22 17:29
 */
@Data
public class User {
    private String name;
    private int age;

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
