package com.welab.search;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class DataFactory {
    public static DataFactory dataFactory = new DataFactory();

    private DataFactory() {
    }

    public DataFactory getInstance() {
        return dataFactory;
    }

    public static List<String> getInitJsonData() {
        List<String> list = new ArrayList<String>();
        String data1 = JSON.toJSONString(new Blog(1, "git简介", "2016-06-19", "SVN与Git最主要的区别..."));
        String data2 = JSON.toJSONString(new Blog(2, "Java中泛型的介绍与简单使用", "2016-06-19", "学习目标 掌握泛型的产生意义..."));
        String data3 = JSON.toJSONString(new Blog(3, "SQL基本操作", "2016-06-19", "基本操作：CRUD ..."));
        String data4 = JSON.toJSONString(new Blog(4, "Hibernate框架基础", "2016-06-19", "Hibernate框架基础..."));
        String data5 = JSON.toJSONString(new Blog(5, "Shell基本知识", "2016-06-19", "Shell是什么..."));
        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        list.add(data5);
        return list;
    }

}