package com.welab.search;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.elasticsearch.common.xcontent.XContentFactory.*;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class ElasticSearch {

    private Client client = null;

    @Before
    public void testStart(){
        /* 创建客户端 */
        try {
            client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用json方式插入数据
     */
    @Test
    public void addJsonIndex() {

        List<String> jsonData = DataFactory.getInitJsonData();
        for (int i = 0; i < jsonData.size(); i++) {
            IndexResponse response = client.prepareIndex("blog", "article").setSource(jsonData.get(i)).get();
            if (response.isCreated()) {
               System.out.println("创建成功!");
            }
        }
        client.close();

    }

    /**
     * 保用field
     * @throws IOException
     */
    @Test
    public void addFieldtIndex() throws IOException {
        IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                ).get();
        if (response.isCreated()) {
            System.out.println("创建成功!");
        }
    }

    /**
     * 查询索引
     */
    @Test
    public void queryIndex(){
        //1、创建QueryBuilder 在title查找包含sql
        QueryBuilder qb1 = termQuery("title", "sql");

        //2、执行查询
        SearchResponse response = client.prepareSearch("blog").setTypes("article")
                .setQuery(qb1).execute().actionGet();
        //3、处理查询结果
        SearchHits hits = response.getHits();
        if (hits.totalHits() > 0){
            for(SearchHit hit : hits){
                System.out.println("score:"+hit.getScore()+":\t"+hit.getSource());// .get("title")
            }
        }else{
            System.out.println("没有查询到结果");
        }
    }

    /**
     * 多列索引查找
     */
    @Test
    public void queryIndex2Param(){
        //1、创建QueryBuilder 查找在title和content上包含git
        QueryBuilder qb2= QueryBuilders.multiMatchQuery("git", "title","content");
        //2、执行查询
        SearchResponse response = client.prepareSearch("blog").setTypes("article")
                .setQuery(qb2).execute().actionGet();
        //3、处理查询结果
        SearchHits hits = response.getHits();
        if (hits.totalHits() > 0){
            for(SearchHit hit : hits){
                System.out.println("score:"+hit.getScore()+":\t"+hit.getSource());// .get("title")
            }
        }else{
            System.out.println("没有查询到结果");
        }
    }

    /**
     * 更新索引
     */
    @Test
    public void updateIndex(){
        try {
            // 方法一:创建一个UpdateRequest,然后将其发送给client.
            UpdateRequest request = new UpdateRequest();
            request.index("blog");
            request.type("article");
            request.id("AVe8zdLaCQfjVNdkBVM8");
            request.doc(jsonBuilder().startObject().field("content", "学习目标 掌握java泛型的产生意义ssss").endObject());
            client.update(request).get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateIndex2() {
        //prepareUpdate() 使用doc更新索引
        try {
            client.prepareUpdate("blog", "article", "AVe8zdBOCQfjVNdkBVM7")
                    .setDoc(jsonBuilder().startObject().field("content", "SVN与Git对比。。。").endObject()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void updateIndex3() {
        // 方法四: 增加新的字段
        try {
            UpdateRequest updateRequest = new UpdateRequest("blog", "article", "AVe8zdBOCQfjVNdkBVM7")
                    .doc(jsonBuilder().startObject().field("commet", "0").endObject());
            client.update(updateRequest).get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果文档不存在则创建新的索引
     * @throws IOException
     */
    @Test
    public void updateIndex4() throws IOException {
        IndexRequest indexRequest = new IndexRequest("blog", "article", "10")
                .source(jsonBuilder().startObject()
                        .field("id", "10")
                        .field("title","Git安装10")
                        .field("content", "学习目标 git。。。10").endObject());

        UpdateRequest updateRequest = new UpdateRequest("blog", "article", "10")
                .doc(jsonBuilder().startObject().field("title", "Git安装").field("content", "学习目标 git。。。").endObject())
                .upsert(indexRequest);

        client.update(updateRequest);

    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndexValue(){
        DeleteResponse dResponse = client.prepareDelete("blog", "article", "10").execute().actionGet();
        if (dResponse.isFound()) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败");
        }
    }

    // 判断索引是否存在 传入参数为索引库名称
    @Test
    public void isIndexExists() {
        String indexName = "articles";
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);
        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(inExistsRequest).actionGet();

        if (inExistsResponse.isExists()) {
            System.out.println("索引"+indexName+"存在");
        } else {
            System.out.println("索引"+indexName+"不存在");
        }
    }

    /**
     * 删除索引库
     */
    @Test
    public void deleteIndex(){

        String indexName = "articles";
        DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName).execute().actionGet();
        if (dResponse.isAcknowledged()) {
            System.out.println("delete index "+indexName+"  successfully!");
        }else{
            System.out.println("Fail to delete index "+indexName);
        }
    }

    @After
    public void endTest(){
        if(client != null){
            client.close();
        }
    }

}