package com.quaint.demo.es.config;

import com.quaint.demo.es.event.DataChangeInterceptor;
import com.quaint.demo.es.helper.DemoEntityMapper;
import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;

/**
 * @author quaint
 * @date 2019-12-26 17:52
 */
@Configuration
public class ElasticSearchConfig {

    /**
     * 创建elasticsearchTemplate
     * @param client
     * @param converter
     * @return
     */
    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(Client client, ElasticsearchConverter converter){
        return new ElasticsearchTemplate(client, converter,new DefaultResultMapper(converter.getMappingContext(),new DemoEntityMapper()));
    }

    /**
     * 数据变化插件
     * @return
     */
    @Bean
    public DataChangeInterceptor dataChangeInterceptor(){
        return new DataChangeInterceptor();
    }

}
