package com.welab.boot;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by WXQ on 2016/10/13.
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = {"com.welab.boot"})
public class ElasticSearchConfig {

    @Resource
    private Environment environment;

    @Bean
    public Client client() {
        Client client = null;
        try {
            String server = environment.getProperty("elasticsearch.host");
            String port = environment.getProperty("elasticsearch.port");
            client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(server), Integer.parseInt(port)));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(client());
    }

}
