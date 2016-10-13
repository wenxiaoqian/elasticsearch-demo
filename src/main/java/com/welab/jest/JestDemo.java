package com.welab.jest;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.mapping.PutMapping;
import org.elasticsearch.common.settings.Settings;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 *
 * @desc 使用jest工具操作elasticsearch
 * @author xiaoqian
 *
 */
public class JestDemo {

    private JestClient client = null;

    @Before
    public void beginTest(){
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://localhost:9200")
                .multiThreaded(true)
                .build());
        client = factory.getObject();
    }

    @Test
    public void addIndexJson() throws IOException {



    }


    @After
    public void endTest(){
        if(client != null){
            client.shutdownClient();
        }
    }
}
